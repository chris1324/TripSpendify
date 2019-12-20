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
import com.example.domain.Trip.domainevent.TripExpenseAdded;
import com.example.domain.Trip.tripbook.TripBook;

import java.util.logging.Logger;

public class AfterTripExpenseAdded extends DomainEventHandler<TripExpenseAdded> {

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

    public AfterTripExpenseAdded(TripBookRepository tripBookRepos,
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
        super(DomainEventEnum.TRIP_EXPENSE_ADDED);

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
    public void onEventDispatched(TripExpenseAdded domainEvent) {
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
        final Outcome<BudgetRecordService.Err.AddRecord> budgetRecordOutcome = mBudgetRecordService.addRecord(
                tripBook,
                budgetBook,
                domainEvent.tripExpenseID);
        if (budgetRecordOutcome.isErr()) this.handleBudgetRecordErr(budgetRecordOutcome);

        // -- cashBook
        final Outcome<CashRecordService.Err.Add.TripExpense> cashRecordOutcome = mCashRecordService.addRecord(
                tripBook,
                cashBook,
                mCashRecordFactory,
                domainEvent.tripExpenseID);
        if (cashRecordOutcome.isErr()) this.handleCashRecordErr(cashRecordOutcome);

        // -- collBook
        final Outcome<CollectibleRecordService.Err.AddRecord> collRecordOutcome = mCollectibleRecordService.addRecord(
                tripBook,
                collBook,
                mCollRecordFactory,
                domainEvent.tripExpenseID);
        if (collRecordOutcome.isErr()) this.handleCollRecordErr(collRecordOutcome);

        // -- persExpBook
        final Outcome<PersExpRecordService.Err.AddRecord> persExpRecordOutcome = mPersExpRecordService.addRecord(
                tripBook,
                persExpBook,
                domainEvent.tripExpenseID);
        if (persExpRecordOutcome.isErr()) this.handlePersExpRecordErr(persExpRecordOutcome);

    }

    // region helper method ------------------------------------------------------------------------
    private void handleBudgetRecordErr(Outcome<BudgetRecordService.Err.AddRecord> budgetRecordOutcome) {
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

    private void handleCashRecordErr(Outcome<CashRecordService.Err.Add.TripExpense> cashRecordOutcome) {
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

    private void handleCollRecordErr(Outcome<CollectibleRecordService.Err.AddRecord> collRecordOutcome) {
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

    private void handlePersExpRecordErr(Outcome<PersExpRecordService.Err.AddRecord> persExpRecordOutcome) {
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
