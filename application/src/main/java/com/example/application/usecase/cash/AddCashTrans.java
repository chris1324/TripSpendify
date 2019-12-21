package com.example.application.usecase.cash;

import com.example.application.common.repository.CashBookRepository;
import com.example.application.common.repository.TripBookRepository;
import com.example.application.common.usecase.CommandUseCase;
import com.example.domain.Cash.cashBook.CashBook;
import com.example.domain.Cash.cashBook.CashRecordFactory;
import com.example.domain.Cash.cashBook.CashTransactionService;
import com.example.domain.Cash.cashTransaction.CashTransaction;
import com.example.domain.Common.errorhanding.exception.ImpossibleState;
import com.example.domain.Common.errorhanding.exception.UnexpectedEnumValue;
import com.example.domain.Common.errorhanding.exception.UnhandledError;
import com.example.domain.Common.errorhanding.outcome.Outcome;
import com.example.domain.Common.errorhanding.result.Result;
import com.example.domain.Common.sharedValueObject.date.Date;
import com.example.domain.Common.sharedValueObject.id.ID;
import com.example.domain.Common.sharedValueObject.note.Note;
import com.example.domain.Common.sharedValueObject.numeric.MonetaryAmount;
import com.example.domain.Trip.tripBook.TripBook;

import java.math.BigDecimal;

public class AddCashTrans extends CommandUseCase<AddCashTrans.RequestModel, AddCashTrans.Err> {

    // region Variables and Constructor ------------------------------------------------------------
    private final TripBookRepository mTripBkRepo;
    private final CashBookRepository mCashBkRepo;
    private final CashRecordFactory mRecordFactory;
    private final CashTransactionService mCashTransactionService;

    public AddCashTrans(TripBookRepository tripBkRepo,
                        CashBookRepository cashBkRepo,
                        CashRecordFactory recordFactory,
                        CashTransactionService cashTransactionService) {
        mTripBkRepo = tripBkRepo;
        mCashBkRepo = cashBkRepo;
        mRecordFactory = recordFactory;
        mCashTransactionService = cashTransactionService;
    }

    // endregion Variables and Constructor ---------------------------------------------------------

    // region RequestModel  ------------------------------------------------------------------------

    public static class RequestModel {
        public enum TransType {
            WITHDRAWAL,
            DEPOSIT,
            ADJUSTMENT_UP,
            ADJUSTMENT_DOWN;

            private CashTransaction.Type getModelEnum() {
                switch (this) {
                    case WITHDRAWAL:
                        return CashTransaction.Type.WITHDRAWAL;
                    case DEPOSIT:
                        return CashTransaction.Type.DEPOSIT;
                    case ADJUSTMENT_UP:
                        return CashTransaction.Type.ADJUSTMENT_UP;
                    case ADJUSTMENT_DOWN:
                        return CashTransaction.Type.ADJUSTMENT_DOWN;
                    default:
                        throw new UnexpectedEnumValue();
                }
            }
        }

        final String tripBookId;
        final long date;
        final String note;
        final String amount;
        final TransType mTransType;

        public RequestModel(String tripBookId, long date, String note, String amount, TransType transType) {
            this.tripBookId = tripBookId;
            this.date = date;
            this.note = note;
            this.amount = amount;
            mTransType = transType;
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

        // CashTransaction
        // -- create
        final Result<CashTransaction, CashTransaction.Err.Create> cashTrans = CashTransaction.create(
                ID.newId(),
                date.getValue().orElseThrow(ImpossibleState::new),
                note.getValue().orElseThrow(ImpossibleState::new),
                amount.getValue().orElseThrow(ImpossibleState::new),
                requestModel.mTransType.getModelEnum());

        // -- check and handle Err
        if (cashTrans.isErr()) return this.handleCashTransErr(cashTrans);

        // TripBook & CashBook
        ID tripBookId = ID.existingId(requestModel.tripBookId);
        TripBook tripBook = mTripBkRepo.getTripBookById(tripBookId);
        CashBook cashBook = mCashBkRepo.getByTripBkId(tripBookId);

        // -- check if exist
        if (tripBook == null) return Outcome.err(Err.TRIP_ID_INVALID);
        if (cashBook == null) throw new ImpossibleState();

        // CashTransactionService
        // -- addCashTrans
        Outcome<CashTransactionService.Err.CashTrans> outcome = mCashTransactionService.addCashTrans(
                tripBook,
                cashBook,
                mRecordFactory,
                cashTrans.getValue().orElseThrow(ImpossibleState::new));

        // -- check and handle Err
        if (outcome.isErr()) return this.handleAddCashTransErr(outcome);

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
        TRIP_DIFFERENT,
        DATE_INVALID
    }

    private Outcome<Err> handleCashTransErr(Result<CashTransaction, CashTransaction.Err.Create> cashTrans) {
        switch (cashTrans.getError().orElseThrow(ImpossibleState::new)) {
            case NULL_ARGUMENT:
                throw new ImpossibleState();
            default:
                throw new UnhandledError();
        }
    }

    private Outcome<Err> handleAddCashTransErr(Outcome<CashTransactionService.Err.CashTrans> outcome) {
        switch (outcome.getError().orElseThrow(ImpossibleState::new)) {
            case TRIP_DIFFERENT:
                return Outcome.err(Err.TRIP_DIFFERENT);
            case DATE_INVALID:
                return Outcome.err(Err.DATE_INVALID);
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
