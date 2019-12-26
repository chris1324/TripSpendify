package com.example.application.Cash.cashTransction.usecase;

import com.example.application.Cash.cashBook.CashBookRepository;
import com.example.application.Cash.cashTransction.dto.CashTransactionDTO;
import com.example.application.Shared.baseUseCase.useCase.UseCase;
import com.example.application.Trip.tripBook.repository.TripBookRepository;
import com.example.application.Shared.baseUseCase.commandUseCase.CommandUseCase;
import com.example.domain.Cash.cashBook.CashBook;
import com.example.domain.Cash.cashBook.CashRecordFactory;
import com.example.domain.Cash.cashBook.CashTransactionService;
import com.example.domain.Cash.cashTransaction.CashTransaction;
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

public class SaveCashTrans extends CommandUseCase<SaveCashTrans.RequestDTO, SaveCashTrans.Err> {

    // region Variables and Constructor ------------------------------------------------------------
    private final TripBookRepository mTripBkRepo;
    private final CashBookRepository mCashBkRepo;
    private final CashRecordFactory mRecordFactory;
    private final CashTransactionService mCashTransactionService;

    public SaveCashTrans(TripBookRepository tripBkRepo,
                         CashBookRepository cashBkRepo,
                         CashRecordFactory recordFactory,
                         CashTransactionService cashTransactionService) {
        mTripBkRepo = tripBkRepo;
        mCashBkRepo = cashBkRepo;
        mRecordFactory = recordFactory;
        mCashTransactionService = cashTransactionService;
    }

    // endregion Variables and Constructor ---------------------------------------------------------

    // region RequestDTO  ------------------------------------------------------------------------

    public static class RequestDTO implements UseCase.RequestDTO {
        public final String tripBookId;
        public final CashTransactionDTO mCashTransDTO;

        public RequestDTO(String tripBookId, CashTransactionDTO cashTransDTO) {
            this.tripBookId = tripBookId;
            mCashTransDTO = cashTransDTO;
        }
    }
    // endregion RequestDTO  ---------------------------------------------------------------------

    @Override
    protected Outcome<Err> execute(RequestDTO requestDTO) {
        // Prepare
         CashTransactionDTO cashTransDTO = requestDTO.mCashTransDTO;

        // Attributes
        // -- create
        ID cashTransId = this.getCashTransId(requestDTO);
        Result<Date, Date.Err.Create> date = Date.create(cashTransDTO.date);
        Result<Note, Note.Err.Create> note = Note.create(cashTransDTO.note);
        Result<MonetaryAmount, MonetaryAmount.Err.Create> amount = MonetaryAmount.create(new BigDecimal(cashTransDTO.amount));

        // -- check and handle Err
        if (date.isErr()) return this.handleDateErr(date);
        if (note.isErr()) return this.handleNoteErr(note);
        if (amount.isErr()) return this.handleAmountErr(amount);

        // CashTransaction
        // -- create
        final Result<CashTransaction, CashTransaction.Err.Create> cashTrans = CashTransaction.create(
                cashTransId,
                date.getValue().orElseThrow(ImpossibleState::new),
                note.getValue().orElseThrow(ImpossibleState::new),
                amount.getValue().orElseThrow(ImpossibleState::new),
                this.getTransType(cashTransDTO.mTransType));

        // -- check and handle Err
        if (cashTrans.isErr()) return this.handleCashTransErr(cashTrans);

        // TripBook & CashBook
        ID tripBookId = ID.existingId(requestDTO.tripBookId);
        TripBook tripBook = mTripBkRepo.getById(tripBookId);
        CashBook cashBook = mCashBkRepo.getByTripId(tripBookId);

        // -- check if exist
        if (tripBook == null) return Outcome.err(Err.TRIP_ID_INVALID);
        if (cashBook == null) throw new ImpossibleState();

        // CashTransactionService
        // -- saveCashTrans
        Outcome<CashTransactionService.Err.CashTrans> outcome = mCashTransactionService.saveCashTrans(
                tripBook,
                cashBook,
                mRecordFactory,
                cashTrans.getValue().orElseThrow(ImpossibleState::new));

        // -- check and handle Err
        if (outcome.isErr()) return this.handleSaveCashTransErr(outcome);

        // Success
        return Outcome.ok();
    }



    // region helper method ------------------------------------------------------------------------
    private ID getCashTransId(RequestDTO requestDTO) {
        final String idInString = requestDTO.mCashTransDTO.cashTransId;
        if (idInString == null) return ID.newId();
        return ID.existingId(idInString);
    }

    private CashTransaction.Type getTransType(CashTransactionDTO.TransType transType) {
        switch (transType) {
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
    // endregion helper method ---------------------------------------------------------------------

    // region Error handing ------------------------------------------------------------------------
    public enum Err implements UseCase.ResponseDTO{
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

    private Outcome<Err> handleSaveCashTransErr(Outcome<CashTransactionService.Err.CashTrans> outcome) {
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
