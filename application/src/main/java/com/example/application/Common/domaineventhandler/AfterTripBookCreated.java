package com.example.application.Common.domaineventhandler;

import com.example.application.Common.repository.BudgetBookRepository;
import com.example.application.Common.repository.CashBookRepository;
import com.example.application.Common.repository.CollectibleBookRepository;
import com.example.application.Common.repository.PersExpBookRepository;
import com.example.domain.Budget.budgetbook.BudgetBook;
import com.example.domain.Cash.cashbook.CashBook;
import com.example.domain.Collectible.collectiblebook.CollectibleBook;
import com.example.domain.Common.domaineventbus.DomainEventEnum;
import com.example.domain.Common.domaineventbus.DomainEventHandler;
import com.example.domain.Common.errorhanding.exception.ImpossibleState;
import com.example.domain.Common.sharedvalueobject.id.ID;
import com.example.domain.PersExpense.persexpbook.PersExpBook;
import com.example.domain.Trip.domainevent.TripBookCreated;

import java.util.ArrayList;

public class AfterTripBookCreated extends DomainEventHandler<TripBookCreated> {


    // region Variables and Constructor ------------------------------------------------------------
    private final BudgetBookRepository mBudgetBookRepository;
    private final CashBookRepository mCashBookRepository;
    private final CollectibleBookRepository mCollectibleBookRepository;
    private final PersExpBookRepository mPersExpBookRepository;

    public AfterTripBookCreated(BudgetBookRepository budgetBookRepository,
                                CashBookRepository cashBookRepository,
                                CollectibleBookRepository collectibleBookRepository,
                                PersExpBookRepository persExpBookRepository) {
        super(DomainEventEnum.TRIP_BOOK_CREATE);
        mBudgetBookRepository = budgetBookRepository;
        mCashBookRepository = cashBookRepository;
        mCollectibleBookRepository = collectibleBookRepository;
        mPersExpBookRepository = persExpBookRepository;
    }
    // endregion Variables and Constructor ---------------------------------------------------------

    @Override
    public void onEventDispatched(TripBookCreated domainEvent) {
        this.createBudgetBook(domainEvent);
        this.createCashBook(domainEvent);
        this.createCollBook(domainEvent);
        this.createPersExpBook(domainEvent);
    }

    // region helper method ------------------------------------------------------------------------
    private void createBudgetBook(TripBookCreated domainEvent) {
        mBudgetBookRepository.save(BudgetBook
                .create(
                        ID.newId(),
                        domainEvent.tripId,
                        new ArrayList<>(),
                        new ArrayList<>()
                )
                .getValue()
                .orElseThrow(ImpossibleState::new));
    }

    private void createCashBook(TripBookCreated domainEvent) {
        mCashBookRepository.save(CashBook
                .create(
                        ID.newId(),
                        domainEvent.tripId,
                        new ArrayList<>(),
                        new ArrayList<>()
                )
                .getValue()
                .orElseThrow(ImpossibleState::new));
    }

    private void createCollBook(TripBookCreated domainEvent) {
        mCollectibleBookRepository.save(CollectibleBook
                .create(
                        ID.newId(),
                        domainEvent.tripId,
                        new ArrayList<>(),
                        new ArrayList<>()).getValue()
                .orElseThrow(ImpossibleState::new));
    }

    private void createPersExpBook(TripBookCreated domainEvent) {
        mPersExpBookRepository.save(PersExpBook
                .create(
                        ID.newId(),
                        domainEvent.tripId,
                        new ArrayList<>()
                )
                .getValue()
                .orElseThrow(ImpossibleState::new));
    }
    // endregion helper method ---------------------------------------------------------------------
}