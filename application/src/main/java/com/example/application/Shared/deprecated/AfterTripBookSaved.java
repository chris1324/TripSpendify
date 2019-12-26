package com.example.application.Shared.deprecated;

import com.example.application.Budget.budgetBook.BudgetBookRepository;
import com.example.application.Cash.cashBook.CashBookRepository;
import com.example.application.Collectible.collBook.CollectibleBookRepository;
import com.example.application.PersonalExpense.PersExpBookRepository;
import com.example.domain.Budget.budgetBook.BudgetBook;
import com.example.domain.Cash.cashBook.CashBook;
import com.example.domain.Collectible.collectibleBook.CollectibleBook;
import com.example.domain.Shared.domainEventBus.DomainEventEnum;
import com.example.domain.Shared.domainEventBus.DomainEventHandler;
import com.example.domain.Shared.errorhanding.exception.ImpossibleState;
import com.example.domain.Shared.valueObject.id.ID;
import com.example.domain.PersernalExpense.persExpBook.PersExpBook;
import com.example.domain.Trip.tripEvent.TripBookSaved;

import java.util.ArrayList;

@Deprecated
public class AfterTripBookSaved extends DomainEventHandler<TripBookSaved> {

    // region Variables and Constructor ------------------------------------------------------------
    private final BudgetBookRepository mBudgetBookRepository;
    private final CashBookRepository mCashBookRepository;
    private final CollectibleBookRepository mCollectibleBookRepository;
    private final PersExpBookRepository mPersExpBookRepository;

    public AfterTripBookSaved(BudgetBookRepository budgetBookRepository,
                              CashBookRepository cashBookRepository,
                              CollectibleBookRepository collectibleBookRepository,
                              PersExpBookRepository persExpBookRepository) {
        super(DomainEventEnum.TRIP_BOOK_SAVED);
        mBudgetBookRepository = budgetBookRepository;
        mCashBookRepository = cashBookRepository;
        mCollectibleBookRepository = collectibleBookRepository;
        mPersExpBookRepository = persExpBookRepository;
    }
    // endregion Variables and Constructor ---------------------------------------------------------

    @Override
    public void onEventDispatched(TripBookSaved domainEvent) {
        ID tripId = domainEvent.tripId;
        if (!(mBudgetBookRepository.exist(tripId))) this.createBudgetBook(domainEvent);
        if (!(mCashBookRepository.exist(tripId))) this.createCashBook(domainEvent);
        if (!(mCollectibleBookRepository.exist(tripId))) this.createCollBook(domainEvent);
        if (!(mPersExpBookRepository.exist(tripId))) this.createPersExpBook(domainEvent);
    }

    // region helper method ------------------------------------------------------------------------
    private void createBudgetBook(TripBookSaved domainEvent) {
        mBudgetBookRepository.save(BudgetBook
                .create(
                        domainEvent.tripId,
                        new ArrayList<>(),
                        new ArrayList<>()
                )
                .getValue()
                .orElseThrow(ImpossibleState::new));
    }

    private void createCashBook(TripBookSaved domainEvent) {
        mCashBookRepository.save(CashBook
                .create(
                        domainEvent.tripId,
                        new ArrayList<>(),
                        new ArrayList<>()
                )
                .getValue()
                .orElseThrow(ImpossibleState::new));
    }

    private void createCollBook(TripBookSaved domainEvent) {
        mCollectibleBookRepository.save(CollectibleBook
                .create(
                        domainEvent.tripId,
                        new ArrayList<>(),
                        new ArrayList<>())
                .getValue()
                .orElseThrow(ImpossibleState::new));
    }

    private void createPersExpBook(TripBookSaved domainEvent) {
        mPersExpBookRepository.save(PersExpBook
                .create(
                        domainEvent.tripId,
                        new ArrayList<>()
                )
                .getValue()
                .orElseThrow(ImpossibleState::new));
    }
    // endregion helper method ---------------------------------------------------------------------
}
