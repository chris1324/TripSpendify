package com.example.application.Collectible.usecase;

import com.example.application.Collectible.dto.collTransactionDTO.CollTransactionDTO;
import com.example.application.Collectible.repository.CollectibleBookRepository;
import com.example.application.Shared.baseUseCase.useCase.UseCase;
import com.example.application.Trip.repository.TripBookRepository;
import com.example.application.Shared.baseUseCase.commandUseCase.CommandUseCase;
import com.example.domain.Collectible.collectibleBook.CollectibleBook;
import com.example.domain.Collectible.collectibleBook.CollectibleRecordFactory;
import com.example.domain.Collectible.collectibleBook.CollectibleTransactionService;
import com.example.domain.Collectible.collectibleTransaction.CollectibleTransaction;
import com.example.domain.Shared.errorhanding.exception.ImpossibleState;
import com.example.domain.Shared.errorhanding.exception.UnexpectedEnumValue;
import com.example.domain.Shared.errorhanding.exception.UnhandledError;
import com.example.domain.Shared.errorhanding.outcome.Outcome;
import com.example.domain.Shared.errorhanding.result.Result;
import com.example.domain.Shared.valueObject.date.Date;
import com.example.domain.Shared.valueObject.id.ID;
import com.example.domain.Shared.valueObject.note.Note;
import com.example.domain.Shared.valueObject.numeric.MonetaryAmount;
import com.example.domain.Trip.tripBook.TripBook;

import java.math.BigDecimal;

public class SaveCollTrans extends CommandUseCase<SaveCollTrans.RequestDTO, SaveCollTrans.Err> {

    // region Variables and Constructor ------------------------------------------------------------
    private final CollectibleTransactionService mCollTransService;
    private final CollectibleRecordFactory mRecordFactory;
    private final TripBookRepository mTripBookRepository;
    private final CollectibleBookRepository mCollBookRepository;

    public SaveCollTrans(CollectibleTransactionService collTransService,
                         CollectibleRecordFactory recordFactory,
                         TripBookRepository tripBookRepository,
                         CollectibleBookRepository collBookRepository) {
        mCollTransService = collTransService;
        mRecordFactory = recordFactory;
        mTripBookRepository = tripBookRepository;
        mCollBookRepository = collBookRepository;
    }

    // endregion Variables and Constructor ---------------------------------------------------------

    // region RequestDTO  ------------------------------------------------------------------------
    public static class RequestDTO implements UseCase.RequestDTO {
        public final String tripId;
        public final CollTransactionDTO mCollTransDTO;

        public RequestDTO(String tripId, CollTransactionDTO collTransDTO) {
            this.tripId = tripId;
            mCollTransDTO = collTransDTO;
        }
    }
    // endregion RequestDTO  ---------------------------------------------------------------------

    @Override
    protected Outcome<Err> execute(RequestDTO requestDTO) {
        // Prepare
        CollTransactionDTO collTransDTO = requestDTO.mCollTransDTO;

        // Attributes
        // -- create
        ID collTransId = this.getCollTransId(requestDTO);
        Result<Date, Date.Err.Create> date = Date.create(collTransDTO.date);
        Result<Note, Note.Err.Create> note = Note.create(collTransDTO.note);
        Result<MonetaryAmount, MonetaryAmount.Err.Create> amount = MonetaryAmount.create(new BigDecimal(collTransDTO.amount));

        // -- check and handle Err
        if (date.isErr()) return this.handleDateErr(date);
        if (note.isErr()) return this.handleNoteErr(note);
        if (amount.isErr()) return this.handleAmountErr(amount);

        // CollectibleTransaction
        // -- create
        Result<CollectibleTransaction, CollectibleTransaction.Err.Create> collTrans = CollectibleTransaction.create(
                collTransId,
                date.getValue().orElseThrow(ImpossibleState::new),
                note.getValue().orElseThrow(ImpossibleState::new),
                amount.getValue().orElseThrow(ImpossibleState::new),
                this.getTransType(collTransDTO.mTransType),
                ID.existingId(collTransDTO.involvedMemberId),
                this.getPayer(collTransDTO.mPayer)
        );

        // -- check and handle Err
        if (collTrans.isErr()) return this.handleCollTransErr(collTrans);

        // TripBook & CollBook
        // -- get from repo
        ID tripBookId = ID.existingId(requestDTO.tripId);
        TripBook tripBook = mTripBookRepository.getById(tripBookId);
        CollectibleBook collectibleBook = mCollBookRepository.getByTripId(tripBookId);

        // -- check if exist
        if (tripBook == null) return Outcome.err(Err.TRIP_ID_INVALID);
        if (collectibleBook == null) throw new ImpossibleState();

        // CollectibleTransactionService
        // -- SaveCollTrans
        Outcome<CollectibleTransactionService.Err.SaveCollTrans> outcome = mCollTransService.saveCollTrans(
                tripBook,
                collectibleBook,
                mRecordFactory,
                collTrans.getValue().orElseThrow(ImpossibleState::new));

        // -- check and handle Err
        if (outcome.isErr()) return this.handleSaveCollTransErr(outcome);

        // Success
        return Outcome.ok();
    }


    // region helper method ------------------------------------------------------------------------
    private ID getCollTransId(RequestDTO requestModel) {
        String idInString = requestModel.mCollTransDTO.collTransId;
        if (idInString == null) return ID.newId();
        return ID.existingId(idInString);
    }

    private CollectibleTransaction.Type getTransType(CollTransactionDTO.TransType transType) {
        switch (transType) {
            case CONTRIBUTION:
                return CollectibleTransaction.Type.CONTRIBUTION;
            case SETTLEMENT:
                return CollectibleTransaction.Type.SETTLEMENT;
            default:
                throw new UnexpectedEnumValue();
        }
    }

    private CollectibleTransaction.Payer getPayer(CollTransactionDTO.Payer payer) {
        switch (payer) {
            case USER:
                return CollectibleTransaction.Payer.USER;
            case MEMBER:
                return CollectibleTransaction.Payer.MEMBER;
            default:
                throw new UnexpectedEnumValue();
        }
    }
    // endregion helper method ---------------------------------------------------------------------

    // region Error handing ------------------------------------------------------------------------
    public enum Err implements UseCase.ResponseDTO {
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

    private Outcome<Err> handleSaveCollTransErr(Outcome<CollectibleTransactionService.Err.SaveCollTrans> outcome) {
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
