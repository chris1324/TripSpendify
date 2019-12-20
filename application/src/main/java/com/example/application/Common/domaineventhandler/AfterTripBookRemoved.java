package com.example.application.Common.domaineventhandler;

import com.example.application.Common.repository.BudgetBookRepository;
import com.example.application.Common.repository.CashBookRepository;
import com.example.application.Common.repository.CollectibleBookRepository;
import com.example.application.Common.repository.PersExpBookRepository;
import com.example.domain.Common.domaineventbus.DomainEventEnum;
import com.example.domain.Common.domaineventbus.DomainEventHandler;
import com.example.domain.Trip.domainevent.TripBookRemoved;

public class AfterTripBookRemoved extends DomainEventHandler<TripBookRemoved> {


    // region Variables and Constructor ------------------------------------------------------------
    private final BudgetBookRepository mBudgetBookRepository;
    private final CashBookRepository mCashBookRepository;
    private final CollectibleBookRepository mCollectibleBookRepository;
    private final PersExpBookRepository mPersExpBookRepository;

    public AfterTripBookRemoved(BudgetBookRepository budgetBookRepository,
                                CashBookRepository cashBookRepository,
                                CollectibleBookRepository collectibleBookRepository,
                                PersExpBookRepository persExpBookRepository) {
        super(DomainEventEnum.TRIP_BOOK_REMOVED);
        mBudgetBookRepository = budgetBookRepository;
        mCashBookRepository = cashBookRepository;
        mCollectibleBookRepository = collectibleBookRepository;
        mPersExpBookRepository = persExpBookRepository;
    }

    // endregion Variables and Constructor ---------------------------------------------------------


    @Override
    public void onEventDispatched(TripBookRemoved domainEvent) {
        mBudgetBookRepository.removeByTripBkID(domainEvent.tripId);
        mCashBookRepository.removeByTripBkID(domainEvent.tripId);
        mCollectibleBookRepository.removeByTripBkID(domainEvent.tripId);
        mPersExpBookRepository.removeByTripBkID(domainEvent.tripId);
    }

}
