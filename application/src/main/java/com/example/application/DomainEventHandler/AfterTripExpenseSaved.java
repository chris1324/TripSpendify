package com.example.application.DomainEventHandler;

import com.example.application.Budget.budgetBook.BudgetBookRepository;
import com.example.application.Cash.cashBook.CashBookRepository;
import com.example.application.Collectible.collBook.CollectibleBookRepository;
import com.example.application.PersonalExpense.PersExpBookRepository;
import com.example.application.Trip.tripBook.repository.TripBookRepository;
import com.example.domain.Budget.budgetBook.BudgetBook;
import com.example.domain.Budget.budgetBook.BudgetRecordService;
import com.example.domain.Cash.cashBook.CashBook;
import com.example.domain.Cash.cashBook.CashRecordFactory;
import com.example.domain.Cash.cashBook.CashRecordService;
import com.example.domain.Collectible.collectibleBook.CollectibleBook;
import com.example.domain.Collectible.collectibleBook.CollectibleRecordFactory;
import com.example.domain.Collectible.collectibleBook.CollectibleRecordService;
import com.example.domain.Shared.domainEventBus.DomainEventEnum;
import com.example.domain.Shared.domainEventBus.DomainEventHandler;
import com.example.domain.Shared.errorhanding.exception.ImpossibleState;
import com.example.domain.Shared.errorhanding.exception.NullArgumentException;
import com.example.domain.Shared.errorhanding.exception.UnhandledError;
import com.example.domain.Shared.errorhanding.guard.Guard;
import com.example.domain.Shared.errorhanding.outcome.Outcome;
import com.example.domain.PersernalExpense.persExpBook.PersExpBook;
import com.example.domain.PersernalExpense.persExpBook.PersExpRecordService;
import com.example.domain.Trip.tripEvent.TripExpenseSaved;
import com.example.domain.Trip.tripBook.TripBook;

public class AfterTripExpenseSaved extends DomainEventHandler<TripExpenseSaved> {

    // region Variables and Constructor ------------------------------------------------------------
    private final TripBookRepository mTripBookRepos;
    private final BudgetBookRepository mBudgetBookRepo;
    private final CashBookRepository mCashBookRepo;
    private final CollectibleBookRepository mCollectibleBookRepo;
    private final PersExpBookRepository mPersExpBookRepo;

    private final CashRecordFactory mCashRecordFactory;
    private final CollectibleRecordFactory mCollRecordFactory;

    private final BudgetRecordService mBudgetRecordService;
    private final CashRecordService mCashRecordService;
    private final CollectibleRecordService mCollectibleRecordService;
    private final PersExpRecordService mPersExpRecordService;

    public AfterTripExpenseSaved(TripBookRepository tripBookRepos,
                                 BudgetBookRepository budgetBookRepo,
                                 CashBookRepository cashBookRepo,
                                 CollectibleBookRepository collectibleBookRepo,
                                 PersExpBookRepository persExpBookRepo,
                                 CashRecordFactory cashRecordFactory,
                                 CollectibleRecordFactory collRecordFactory,
                                 BudgetRecordService budgetRecordService,
                                 CashRecordService cashRecordService,
                                 CollectibleRecordService collectibleRecordService,
                                 PersExpRecordService persExpRecordService) {
        super(DomainEventEnum.TRIP_EXPENSE_SAVED);

        mTripBookRepos = tripBookRepos;
        mBudgetBookRepo = budgetBookRepo;
        mCashBookRepo = cashBookRepo;
        mCollectibleBookRepo = collectibleBookRepo;
        mPersExpBookRepo = persExpBookRepo;

        mCashRecordFactory = cashRecordFactory;
        mCollRecordFactory = collRecordFactory;

        mBudgetRecordService = budgetRecordService;
        mCashRecordService = cashRecordService;
        mCollectibleRecordService = collectibleRecordService;
        mPersExpRecordService = persExpRecordService;
    }


    // endregion Variables and Constructor ---------------------------------------------------------

    @Override
    public void onEventDispatched(TripExpenseSaved domainEvent) {
        // Prepare
        // -- get from repo
        TripBook tripBook = mTripBookRepos.getById(domainEvent.tripBookId);
        BudgetBook budgetBook = mBudgetBookRepo.getByTripId(domainEvent.tripBookId);
        CashBook cashBook = mCashBookRepo.getByTripId(domainEvent.tripBookId);
        CollectibleBook collBook = mCollectibleBookRepo.getByTripId(domainEvent.tripBookId);
        PersExpBook persExpBook = mPersExpBookRepo.getByTripId(domainEvent.tripBookId);

        // -- check if exist
        try {
            Guard.NotNull(tripBook);
            Guard.NotNull(budgetBook);
            Guard.NotNull(cashBook);
            Guard.NotNull(collBook);
            Guard.NotNull(persExpBook);

        } catch (NullArgumentException e) {
            throw new ImpossibleState();
        }

        // Save Record
        // -- budgetBook
        final Outcome<BudgetRecordService.Err.SaveRecord> budgetRecordOutcome = mBudgetRecordService.saveRecord(
                tripBook,
                budgetBook,
                domainEvent.tripExpenseID);
        if (budgetRecordOutcome.isErr()) this.handleBudgetRecordErr(budgetRecordOutcome);

        // -- cashBook
        final Outcome<CashRecordService.Err.Save.TripExpense> cashRecordOutcome = mCashRecordService.saveRecord(
                tripBook,
                cashBook,
                mCashRecordFactory,
                domainEvent.tripExpenseID);
        if (cashRecordOutcome.isErr()) this.handleCashRecordErr(cashRecordOutcome);

        // -- collBook
        final Outcome<CollectibleRecordService.Err.SaveRecord> collRecordOutcome = mCollectibleRecordService.saveRecord(
                tripBook,
                collBook,
                mCollRecordFactory,
                domainEvent.tripExpenseID);
        if (collRecordOutcome.isErr()) this.handleCollRecordErr(collRecordOutcome);

        // -- persExpBook
        final Outcome<PersExpRecordService.Err.SaveRecord> persExpRecordOutcome = mPersExpRecordService.saveRecord(
                tripBook,
                persExpBook,
                domainEvent.tripExpenseID);
        if (persExpRecordOutcome.isErr()) this.handlePersExpRecordErr(persExpRecordOutcome);

    }

    // region helper method ------------------------------------------------------------------------
    private void handleBudgetRecordErr(Outcome<BudgetRecordService.Err.SaveRecord> budgetRecordOutcome) {
        switch (budgetRecordOutcome.getError().orElseThrow(ImpossibleState::new)) {
            case SOURCE_TRANS_NOT_EXIST:
            case DIFFERENT_TRIP:
                throw new ImpossibleState();
            case INVALID_DATE:
            case USER_NOT_SPENDING:
                this.acceptedError();
            default:
                throw new UnhandledError();
        }
    }

    private void handleCashRecordErr(Outcome<CashRecordService.Err.Save.TripExpense> cashRecordOutcome) {
        switch (cashRecordOutcome.getError().orElseThrow(ImpossibleState::new)) {
            case SOURCE_TRANS_NOT_EXIST:
            case DIFFERENT_TRIP:
                throw new ImpossibleState();
            case INVALID_DATE:
            case USER_NOT_PAYING:
                this.acceptedError();
            default:
                throw new UnhandledError();
        }

    }

    private void handleCollRecordErr(Outcome<CollectibleRecordService.Err.SaveRecord> collRecordOutcome) {
        switch (collRecordOutcome.getError().orElseThrow(ImpossibleState::new)) {
            case SOURCE_TRANS_NOT_EXIST:
            case DIFFERENT_TRIP:
                throw new ImpossibleState();
            case NO_BORROWING_OR_LENDING:
                this.acceptedError();
                break;
            default:
                throw new UnhandledError();
        }
    }

    private void handlePersExpRecordErr(Outcome<PersExpRecordService.Err.SaveRecord> persExpRecordOutcome) {
        switch (persExpRecordOutcome.getError().orElseThrow(ImpossibleState::new)) {
            case SOURCE_TRANS_NOT_EXIST:
            case DIFFERENT_TRIP:
                throw new ImpossibleState();
            case USER_NOT_SPENDING:
                this.acceptedError();
                break;
            default:
                throw new UnhandledError();
        }

    }
    // endregion helper method ---------------------------------------------------------------------

    // region AcceptedError  -----------------------------------------------------------------------
    private void acceptedError() {
    }
    // endregion AcceptedError  --------------------------------------------------------------------
}
