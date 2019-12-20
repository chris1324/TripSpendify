package com.example.application.Budget;

import com.example.application.Common.repository.BudgetBookRepository;
import com.example.application.Common.repository.TripBookRepository;
import com.example.application.Common.usecase.CommandUseCase;
import com.example.domain.Budget.budgetbook.BudgetBook;
import com.example.domain.Budget.budgetbook.BudgetTransactionService;
import com.example.domain.Budget.budgettransaction.BudgetTransaction;
import com.example.domain.Common.errorhanding.exception.ImpossibleState;
import com.example.domain.Common.errorhanding.exception.UnhandledError;
import com.example.domain.Common.errorhanding.outcome.Outcome;
import com.example.domain.Common.errorhanding.result.Result;
import com.example.domain.Common.sharedvalueobject.date.Date;
import com.example.domain.Common.sharedvalueobject.id.ID;
import com.example.domain.Common.sharedvalueobject.note.Note;
import com.example.domain.Common.sharedvalueobject.numeric.MonetaryAmount;
import com.example.domain.Trip.tripbook.TripBook;

import java.math.BigDecimal;

public class AddBudgetTrans extends CommandUseCase<AddBudgetTrans.RequestModel, AddBudgetTrans.Err> {

    // region Variables and Constructor ------------------------------------------------------------
    private final TripBookRepository mTripBkRepo;
    private final BudgetBookRepository mBudgetBkRepo;
    private final BudgetTransactionService mBudgetTransService;

    public AddBudgetTrans(TripBookRepository tripBkRepo,
                          BudgetBookRepository budgetBkRepo,
                          BudgetTransactionService budgetTransService) {
        mTripBkRepo = tripBkRepo;
        mBudgetBkRepo = budgetBkRepo;
        mBudgetTransService = budgetTransService;
    }

    // endregion Variables and Constructor ---------------------------------------------------------

    // region RequestModel  ------------------------------------------------------------------------
    public static class RequestModel {
        final String tripBookId;
        final long date;
        final String note;
        final String amount;
        final String categoryId;

        public RequestModel(String tripBookId, long date, String note, String amount, String categoryId) {
            this.tripBookId = tripBookId;
            this.date = date;
            this.note = note;
            this.amount = amount;
            this.categoryId = categoryId;
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

        // BudgetTransaction
        // -- create
        Result<BudgetTransaction, BudgetTransaction.Err.Create> budgetTrans = BudgetTransaction.create(
                ID.newId(),
                date.getValue().orElseThrow(ImpossibleState::new),
                note.getValue().orElseThrow(ImpossibleState::new),
                amount.getValue().orElseThrow(ImpossibleState::new),
                ID.existingId(requestModel.categoryId)
        );

        // -- check and handle Err
        if (budgetTrans.isErr()) return this.handleBudgetTransErr(budgetTrans);

        // TripBook & BudgetBook
        // -- get from repo
        ID tripBookId = ID.existingId(requestModel.tripBookId);
        TripBook tripBook = mTripBkRepo.getTripBookById(tripBookId);
        BudgetBook budgetBook = mBudgetBkRepo.getByTripBkId(tripBookId);

        // -- check if exist
        if (tripBook == null) return Outcome.err(Err.TRIP_ID_INVALID);
        if (budgetBook == null) throw new ImpossibleState();

        // BudgetTransService
        // -- AddBudgetTrans
        Outcome<BudgetTransactionService.Err> outcome = mBudgetTransService.addBudgetTrans(
                tripBook,
                budgetBook,
                budgetTrans.getValue().orElseThrow(ImpossibleState::new));

        // -- check and handle Err
        if (outcome.isErr()) return this.handleAddBudgetTransErr(outcome);

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

    private Outcome<Err> handleBudgetTransErr(Result<BudgetTransaction, BudgetTransaction.Err.Create> budgetTrans) {
        switch (budgetTrans.getError().orElseThrow(ImpossibleState::new)) {
            case NULL_ARGUMENT:
                throw new ImpossibleState();
            default:
                throw new UnhandledError();
        }
    }

    private Outcome<Err> handleAddBudgetTransErr(Outcome<BudgetTransactionService.Err> outcome) {
        switch (outcome.getError().orElseThrow(ImpossibleState::new)){
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