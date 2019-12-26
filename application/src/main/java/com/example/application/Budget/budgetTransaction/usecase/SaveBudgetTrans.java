package com.example.application.Budget.budgetTransaction.usecase;

import com.example.application.Budget.budgetTransaction.dto.BudgetTransactionDTO;
import com.example.application.Budget.budgetBook.BudgetBookRepository;
import com.example.application.Shared.baseUseCase.commandUseCase.CommandUseCase;
import com.example.application.Shared.baseUseCase.useCase.UseCase;
import com.example.application.Trip.tripBook.repository.TripBookRepository;
import com.example.domain.Budget.budgetBook.BudgetBook;
import com.example.domain.Budget.budgetBook.BudgetTransactionService;
import com.example.domain.Budget.budgetTransaction.BudgetTransaction;
import com.example.domain.Shared.errorhanding.exception.ImpossibleState;
import com.example.domain.Shared.errorhanding.exception.UnhandledError;
import com.example.domain.Shared.errorhanding.outcome.Outcome;
import com.example.domain.Shared.errorhanding.result.Result;
import com.example.domain.Shared.valueObject.date.Date;
import com.example.domain.Shared.valueObject.id.ID;
import com.example.domain.Shared.valueObject.note.Note;
import com.example.domain.Shared.valueObject.numeric.MonetaryAmount;
import com.example.domain.Trip.tripBook.TripBook;

import java.math.BigDecimal;

public class SaveBudgetTrans extends CommandUseCase<SaveBudgetTrans.RequestDTO, SaveBudgetTrans.Err> {

    // region Variables and Constructor ------------------------------------------------------------
    private final TripBookRepository mTripBkRepo;
    private final BudgetBookRepository mBudgetBkRepo;
    private final BudgetTransactionService mBudgetTransService;

    public SaveBudgetTrans(TripBookRepository tripBkRepo,
                           BudgetBookRepository budgetBkRepo,
                           BudgetTransactionService budgetTransService) {
        mTripBkRepo = tripBkRepo;
        mBudgetBkRepo = budgetBkRepo;
        mBudgetTransService = budgetTransService;
    }

    // endregion Variables and Constructor ---------------------------------------------------------

    // region RequestDTO  ------------------------------------------------------------------------
    public static class RequestDTO implements UseCase.RequestDTO {
        public final String tripBookId;
        public final BudgetTransactionDTO mBudgetTransDTO;

        public RequestDTO(String tripBookId, BudgetTransactionDTO budgetTransDTO) {
            this.tripBookId = tripBookId;
            mBudgetTransDTO = budgetTransDTO;
        }
    }
    // endregion RequestDTO  ---------------------------------------------------------------------

    @Override
    protected Outcome<Err> execute(RequestDTO requestDTO) {
        // Prepare
        BudgetTransactionDTO budgetTransDTO = requestDTO.mBudgetTransDTO;

        // Attributes
        // -- create
        ID budgetTransId = this.getBudgetTransId(requestDTO);
        Result<Date, Date.Err.Create> date = Date.create(budgetTransDTO.date);
        Result<Note, Note.Err.Create> note = Note.create(budgetTransDTO.note);
        Result<MonetaryAmount, MonetaryAmount.Err.Create> amount = MonetaryAmount.create(new BigDecimal(budgetTransDTO.amount));

        // -- check and handle Err
        if (date.isErr()) return this.handleDateErr(date);
        if (note.isErr()) return this.handleNoteErr(note);
        if (amount.isErr()) return this.handleAmountErr(amount);

        // BudgetTransaction
        // -- create
        Result<BudgetTransaction, BudgetTransaction.Err.Create> budgetTrans = BudgetTransaction.create(
                budgetTransId,
                date.getValue().orElseThrow(ImpossibleState::new),
                note.getValue().orElseThrow(ImpossibleState::new),
                amount.getValue().orElseThrow(ImpossibleState::new),
                ID.existingId(budgetTransDTO.categoryId)
        );

        // -- check and handle Err
        if (budgetTrans.isErr()) return this.handleBudgetTransErr(budgetTrans);

        // TripBook & BudgetBook
        // -- get from repo
        ID tripBookId = ID.existingId(requestDTO.tripBookId);
        TripBook tripBook = mTripBkRepo.getById(tripBookId);
        BudgetBook budgetBook = mBudgetBkRepo.getByTripId(tripBookId);

        // -- check if exist
        if (tripBook == null) return Outcome.err(Err.TRIP_ID_INVALID);
        if (budgetBook == null) throw new ImpossibleState();

        // BudgetTransService
        // -- SaveBudgetTrans
        Outcome<BudgetTransactionService.Err> outcome = mBudgetTransService.saveBudgetTrans(
                tripBook,
                budgetBook,
                budgetTrans.getValue().orElseThrow(ImpossibleState::new));

        // -- check and handle Err
        if (outcome.isErr()) return this.handleSaveBudgetTransErr(outcome);

        // Success
        return Outcome.ok();
    }


    // region helper method ------------------------------------------------------------------------
    private ID getBudgetTransId(RequestDTO requestDTO) {
        final String idInString = requestDTO.mBudgetTransDTO.budgetTransId;
        if (idInString == null) return ID.newId();
        return ID.existingId(idInString);
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
        TRIP_DIFFERENT,
        DATE_INVALID
    }

    private Outcome<Err> handleBudgetTransErr(Result<BudgetTransaction, BudgetTransaction.Err.Create> budgetTrans) {
        switch (budgetTrans.getError().orElseThrow(ImpossibleState::new)) {
            case NULL_ARGUMENT:
                throw new ImpossibleState();
            default:
                throw new UnhandledError();
        }
    }

    private Outcome<Err> handleSaveBudgetTransErr(Outcome<BudgetTransactionService.Err> outcome) {
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
        switch (date.getError().orElseThrow(IllegalAccessError::new)) {
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
