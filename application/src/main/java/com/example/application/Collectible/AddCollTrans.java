package com.example.application.Collectible;

import com.example.application.Common.repository.CollectibleBookRepository;
import com.example.application.Common.repository.TripBookRepository;
import com.example.application.Common.usecase.CommandUseCase;
import com.example.domain.Collectible.collectiblebook.CollectibleBook;
import com.example.domain.Collectible.collectiblebook.CollectibleRecordFactory;
import com.example.domain.Collectible.collectiblebook.CollectibleTransactionService;
import com.example.domain.Collectible.collectibletransaction.CollectibleTransaction;
import com.example.domain.Common.errorhanding.exception.ImpossibleState;
import com.example.domain.Common.errorhanding.exception.UnexpectedEnumValue;
import com.example.domain.Common.errorhanding.exception.UnhandledError;
import com.example.domain.Common.errorhanding.outcome.Outcome;
import com.example.domain.Common.errorhanding.result.Result;
import com.example.domain.Common.sharedvalueobject.date.Date;
import com.example.domain.Common.sharedvalueobject.id.ID;
import com.example.domain.Common.sharedvalueobject.note.Note;
import com.example.domain.Common.sharedvalueobject.numeric.MonetaryAmount;
import com.example.domain.Trip.tripbook.TripBook;

import java.math.BigDecimal;

public class AddCollTrans extends CommandUseCase<AddCollTrans.RequestModel, AddCollTrans.Err> {

    // region Variables and Constructor ------------------------------------------------------------
    private final CollectibleTransactionService mCollTransService;
    private final CollectibleRecordFactory mRecordFactory;
    private final TripBookRepository mTripBookRepository;
    private final CollectibleBookRepository mCollBookRepository;

    public AddCollTrans(CollectibleTransactionService collTransService,
                        CollectibleRecordFactory recordFactory,
                        TripBookRepository tripBookRepository,
                        CollectibleBookRepository collBookRepository) {
        mCollTransService = collTransService;
        mRecordFactory = recordFactory;
        mTripBookRepository = tripBookRepository;
        mCollBookRepository = collBookRepository;
    }

    // endregion Variables and Constructor ---------------------------------------------------------

    // region RequestModel  ------------------------------------------------------------------------
    public static class RequestModel {

        public enum TransType {
            CONTRIBUTION,
            SETTLEMENT;

            private CollectibleTransaction.Type getModelEnum() {
                switch (this) {
                    case CONTRIBUTION:
                        return CollectibleTransaction.Type.CONTRIBUTION;
                    case SETTLEMENT:
                        return CollectibleTransaction.Type.SETTLEMENT;
                    default:
                        throw new UnexpectedEnumValue();
                }
            }
        }

        public enum Payer {
            USER,
            MEMBER;

            private CollectibleTransaction.Payer getModelEnum() {
                switch (this) {
                    case USER:
                        return CollectibleTransaction.Payer.USER;
                    case MEMBER:
                        return CollectibleTransaction.Payer.MEMBER;
                    default:
                        throw new UnexpectedEnumValue();
                }
            }
        }

        final String tripId;

        final long date;
        final String note;
        final String amount;
        final TransType mTransType;

        final String involvedMemberId;
        final Payer mPayer;

        public RequestModel(String tripId,
                            long date,
                            String note,
                            String amount,
                            TransType transType,
                            String involvedMemberId,
                            Payer payer) {
            this.tripId = tripId;
            this.date = date;
            this.note = note;
            this.amount = amount;
            mTransType = transType;
            this.involvedMemberId = involvedMemberId;
            mPayer = payer;
        }
    }
    // endregion RequestModel  ---------------------------------------------------------------------

    @Override
    protected Outcome<Err> execute(RequestModel requestModel) {
        // Attributes
        // -- create
        Result<Date, Date.Err.Create> date = Date.create(requestModel.date);
        Result<Note, Note.Err.Create> note = Note.create(requestModel.note);
        Result<MonetaryAmount, MonetaryAmount.Err.Create> amount = MonetaryAmount.create(new BigDecimal(requestModel.amount));

        // -- check and handle Err
        if (date.isErr()) return this.handleDateErr(date);
        if (note.isErr()) return this.handleNoteErr(note);
        if (amount.isErr()) return this.handleAmountErr(amount);

        // CollectibleTransaction
        // -- create
        Result<CollectibleTransaction, CollectibleTransaction.Err.Create> collTrans = CollectibleTransaction.create(
                ID.newId(),
                date.getValue().orElseThrow(ImpossibleState::new),
                note.getValue().orElseThrow(ImpossibleState::new),
                amount.getValue().orElseThrow(ImpossibleState::new),
                requestModel.mTransType.getModelEnum(),
                ID.existingId(requestModel.involvedMemberId),
                requestModel.mPayer.getModelEnum()
        );

        // -- check and handle Err
        if (collTrans.isErr()) return this.handleCollTransErr(collTrans);

        // TripBook & CollBook
        // -- get from repo
        ID tripBookId = ID.existingId(requestModel.tripId);
        TripBook tripBook = mTripBookRepository.getTripBookById(tripBookId);
        CollectibleBook collectibleBook = mCollBookRepository.getByTripBkId(tripBookId);

        // -- check if exist
        if (tripBook == null) return Outcome.err(Err.TRIP_ID_INVALID);
        if (collectibleBook == null) throw new ImpossibleState();

        // CollectibleTransactionService
        // -- AddCollTrans
        Outcome<CollectibleTransactionService.Err.AddCollTrans> outcome = mCollTransService.addCollTrans(
                tripBook,
                collectibleBook,
                mRecordFactory,
                collTrans.getValue().orElseThrow(ImpossibleState::new));

        // -- check and handle Err
        if (outcome.isErr()) return this.handleAddCollTransErr(outcome);

        // Success
        return Outcome.ok();
    }


    // region helper method ------------------------------------------------------------------------
    // endregion helper method ---------------------------------------------------------------------

    // region Error handing ------------------------------------------------------------------------
    public enum Err {
        TRIP_ID_INVALID,
        DATE_NULL,
        NOTE_NULL,
        AMOUNT_NULL,
        AMOUNT_NEGATIVE,
        AMOUNT_ZERO,
        MEMBER_INVALID,
        TRIP_DIFFERENT
    }

    private Outcome<Err> handleCollTransErr(Result<CollectibleTransaction, CollectibleTransaction.Err.Create> collTrans) {
        switch (collTrans.getError().orElseThrow(ImpossibleState::new)) {
            case NULL_ARGUMENT:
                throw new ImpossibleState();
            default:
                throw new UnhandledError();
        }
    }

    private Outcome<Err> handleAddCollTransErr(Outcome<CollectibleTransactionService.Err.AddCollTrans> outcome) {
        switch (outcome.getError().orElseThrow(ImpossibleState::new)) {
            case MEMBER_INVALID:
                return Outcome.err(Err.MEMBER_INVALID);
            case TRIP_DIFFERENT:
                return Outcome.err(Err.TRIP_DIFFERENT);
            default:
                throw new UnhandledError();
        }
    }

    private Outcome<Err> handleDateErr(Result<Date, Date.Err.Create> date) {
        switch (date.getError().orElseThrow(ImpossibleState::new)) {
            case MILLISECOND_IS_ZERO:
                return Outcome.err(Err.DATE_NULL);
            default:
                throw new UnhandledError();
        }
    }

    private Outcome<Err> handleNoteErr(Result<Note, Note.Err.Create> note) {
        switch (note.getError().orElseThrow(ImpossibleState::new)) {
            case NULL_NOTE:
                return Outcome.err(Err.NOTE_NULL);
            default:
                throw new UnhandledError();
        }
    }

    private Outcome<Err> handleAmountErr(Result<MonetaryAmount, MonetaryAmount.Err.Create> amount) {
        switch (amount.getError().orElseThrow(ImpossibleState::new)) {
            case AMOUNT_NULL:
                return Outcome.err(Err.AMOUNT_NULL);
            case AMOUNT_NEGATIVE:
                return Outcome.err(Err.AMOUNT_NEGATIVE);
            case AMOUNT_ZERO:
                return Outcome.err(Err.AMOUNT_ZERO);
            default:
                throw new UnhandledError();
        }
    }
    // endregion Error handing ---------------------------------------------------------------------
}
