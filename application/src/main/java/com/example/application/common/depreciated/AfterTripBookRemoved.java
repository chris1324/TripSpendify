package com.example.application.common.depreciated;

import com.example.application.common.repository.BudgetBookRepository;
import com.example.application.common.repository.CashBookRepository;
import com.example.application.common.repository.CollectibleBookRepository;
import com.example.application.common.repository.PersExpBookRepository;
import com.example.domain.Common.domaineEventBus.DomainEventEnum;
import com.example.domain.Common.domaineEventBus.DomainEventHandler;
import com.example.domain.Trip.tripEvent.TripBookRemoved;


@Deprecated
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
