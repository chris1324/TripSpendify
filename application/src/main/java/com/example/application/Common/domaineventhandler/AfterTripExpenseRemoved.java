package com.example.application.Common.domaineventhandler;

import com.example.application.Common.repository.BudgetBookRepository;
import com.example.application.Common.repository.CashBookRepository;
import com.example.application.Common.repository.CollectibleBookRepository;
import com.example.application.Common.repository.PersExpBookRepository;
import com.example.application.Common.repository.TripBookRepository;
import com.example.domain.Budget.budgetbook.BudgetBook;
import com.example.domain.Budget.budgetbook.BudgetRecordService;
import com.example.domain.Cash.cashbook.CashBook;
import com.example.domain.Cash.cashbook.CashRecordFactory;
import com.example.domain.Cash.cashbook.CashRecordService;
import com.example.domain.Collectible.collectiblebook.CollectibleBook;
import com.example.domain.Collectible.collectiblebook.CollectibleRecordFactory;
import com.example.domain.Collectible.collectiblebook.CollectibleRecordService;
import com.example.domain.Common.domaineventbus.DomainEventEnum;
import com.example.domain.Common.domaineventbus.DomainEventHandler;
import com.example.domain.Common.errorhanding.exception.ImpossibleState;
import com.example.domain.Common.errorhanding.exception.NullArgumentException;
import com.example.domain.Common.errorhanding.exception.UnhandledError;
import com.example.domain.Common.errorhanding.guard.Guard;
import com.example.domain.Common.errorhanding.outcome.Outcome;
import com.example.domain.PersExpense.persexpbook.PersExpBook;
import com.example.domain.PersExpense.persexpbook.PersExpRecordService;
import com.example.domain.Trip.domainevent.TripExpenseRemoved;
import com.example.domain.Trip.tripbook.TripBook;

public class AfterTripExpenseRemoved extends DomainEventHandler<TripExpenseRemoved> {


    // region Variables and Constructor ------------------------------------------------------------
    private final TripBookRepository mTripBookRepos;
    private final BudgetBookRepository mBudgetBookRepo;
    private final CashBookRepository mCashBookRepo;
    private final CollectibleBookRepository mCollectibleBookRepo;
    private final PersExpBookRepository mPersExpBookRepo;

    private final BudgetRecordService mBudgetRecordService;
    private final CashRecordService mCashRecordService;
    private final CollectibleRecordService mCollectibleRecordService;
    private final PersExpRecordService mPersExpRecordService;

    public AfterTripExpenseRemoved(TripBookRepository tripBookRepos,
                                   BudgetBookRepository budgetBookRepo,
                                   CashBookRepository cashBookRepo,
                                   CollectibleBookRepository collectibleBookRepo,
                                   PersExpBookRepository persExpBookRepo,
                                   BudgetRecordService budgetRecordService,
                                   CashRecordService cashRecordService,
                                   CollectibleRecordService collectibleRecordService,
                                   PersExpRecordService persExpRecordService) {
        super(DomainEventEnum.TRIP_EXPENSE_REMOVED);
        mTripBookRepos = tripBookRepos;
        mBudgetBookRepo = budgetBookRepo;
        mCashBookRepo = cashBookRepo;
        mCollectibleBookRepo = collectibleBookRepo;
        mPersExpBookRepo = persExpBookRepo;
        mBudgetRecordService = budgetRecordService;
        mCashRecordService = cashRecordService;
        mCollectibleRecordService = collectibleRecordService;
        mPersExpRecordService = persExpRecordService;
    }

    // endregion Variables and Constructor ---------------------------------------------------------

    @Override
    public void onEventDispatched(TripExpenseRemoved domainEvent) {
        // Prepare
        // -- get from repo
        TripBook tripBook = mTripBookRepos.getTripBookById(domainEvent.tripBookId);
        BudgetBook budgetBook = mBudgetBookRepo.getByTripBkId(domainEvent.tripBookId);
        CashBook cashBook = mCashBookRepo.getByTripBkId(domainEvent.tripBookId);
        CollectibleBook collBook = mCollectibleBookRepo.getByTripBkId(domainEvent.tripBookId);
        PersExpBook persExpBook = mPersExpBookRepo.getByTripBkId(domainEvent.tripBookId);

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


        // Add Record
        // -- budgetBook
        final Outcome<BudgetRecordService.Err.RemoveRecord> budgetRecordOutcome = mBudgetRecordService.removeRecord(
                tripBook,
                budgetBook,
                domainEvent.tripExpenseID);
        if (budgetRecordOutcome.isErr()) this.handleBudgetRecordErr(budgetRecordOutcome);

        // -- cashBook
        final Outcome<CashRecordService.Err.Remove> cashRecordOutcome = mCashRecordService.removeRecord(
                tripBook,
                cashBook,
                domainEvent.tripExpenseID);
        if (cashRecordOutcome.isErr()) this.handleCashRecordErr(cashRecordOutcome);

        // -- collBook
        final Outcome<CollectibleRecordService.Err.RemoveRecord> collRecordOutcome = mCollectibleRecordService.removeRecord(
                tripBook,
                collBook,
                domainEvent.tripExpenseID);
        if (collRecordOutcome.isErr()) this.handleCollRecordErr(collRecordOutcome);

        // -- persExpBook
        final Outcome<PersExpRecordService.Err.RemoveRecord> persExpRecordOutcome = mPersExpRecordService.removeRecord(
                tripBook,
                persExpBook,
                domainEvent.tripExpenseID);
        if (persExpRecordOutcome.isErr()) this.handlePersExpRecordErr(persExpRecordOutcome);

    }

    // region helper method ------------------------------------------------------------------------
    private void handleBudgetRecordErr(Outcome<BudgetRecordService.Err.RemoveRecord> budgetRecordOutcome) {
        switch (budgetRecordOutcome.getError().orElseThrow(IllegalAccessError::new)) {
            case DIFFERENT_TRIP:
            case SOURCE_STILL_EXIST:
                throw new ImpossibleState();
            default:
                throw new UnhandledError();
        }
    }

    private void handleCashRecordErr(Outcome<CashRecordService.Err.Remove> cashRecordOutcome) {
        switch (cashRecordOutcome.getError().orElseThrow(IllegalAccessError::new)) {
            case DIFFERENT_TRIP:
            case SOURCE_STILL_EXIST:
                throw new ImpossibleState();
            default:
                throw new UnhandledError();
        }
    }

    private void handleCollRecordErr(Outcome<CollectibleRecordService.Err.RemoveRecord> collRecordOutcome) {
        switch (collRecordOutcome.getError().orElseThrow(IllegalAccessError::new)) {
            case DIFFERENT_TRIP:
            case SOURCE_STILL_EXIST:
                throw new ImpossibleState();
            default:
                throw new UnhandledError();
        }
    }

    private void handlePersExpRecordErr(Outcome<PersExpRecordService.Err.RemoveRecord> persExpRecordOutcome) {
        switch (persExpRecordOutcome.getError().orElseThrow(IllegalAccessError::new)) {
            case DIFFERENT_TRIP:
            case SOURCE_STILL_EXIST:
                throw new ImpossibleState();
            default:
                throw new UnhandledError();
        }
    }
    // endregion helper method ---------------------------------------------------------------------

}

