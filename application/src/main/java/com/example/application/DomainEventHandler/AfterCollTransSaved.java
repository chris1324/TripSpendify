package com.example.application.DomainEventHandler;

import com.example.application.Cash.cashBook.CashBookRepository;
import com.example.application.Collectible.collBook.CollectibleBookRepository;
import com.example.application.Trip.tripBook.repository.TripBookRepository;
import com.example.domain.Cash.cashBook.CashBook;
import com.example.domain.Cash.cashBook.CashRecordFactory;
import com.example.domain.Cash.cashBook.CashRecordService;
import com.example.domain.Collectible.collectibleBook.CollectibleBook;
import com.example.domain.Collectible.collectibleEvent.CollTransSaved;
import com.example.domain.Shared.domainEventBus.DomainEventEnum;
import com.example.domain.Shared.domainEventBus.DomainEventHandler;
import com.example.domain.Shared.errorhanding.exception.ImpossibleState;
import com.example.domain.Shared.errorhanding.exception.NullArgumentException;
import com.example.domain.Shared.errorhanding.exception.UnhandledError;
import com.example.domain.Shared.errorhanding.guard.Guard;
import com.example.domain.Shared.errorhanding.outcome.Outcome;
import com.example.domain.Trip.tripBook.TripBook;

public class AfterCollTransSaved extends DomainEventHandler<CollTransSaved> {

    // region Variables and Constructor ------------------------------------------------------------

    private final TripBookRepository mTripBookRepository;
    private final CollectibleBookRepository mCollectibleBookRepository;
    private final CashBookRepository mCashBookRepository;

    private final CashRecordService mCashRecordService;
    private final CashRecordFactory mRecordFactory;

    public AfterCollTransSaved(TripBookRepository tripBookRepository,
                               CollectibleBookRepository collectibleBookRepository,
                               CashBookRepository cashBookRepository,
                               CashRecordService cashRecordService,
                               CashRecordFactory recordFactory) {
        super(DomainEventEnum.COLL_TRANS_SAVED);
        mTripBookRepository = tripBookRepository;
        mCollectibleBookRepository = collectibleBookRepository;
        mCashBookRepository = cashBookRepository;
        mCashRecordService = cashRecordService;
        mRecordFactory = recordFactory;
    }
    // endregion Variables and Constructor ---------------------------------------------------------

    @Override
    public void onEventDispatched(CollTransSaved domainEvent) {
        // Prepare
        // -- get from repo
        TripBook tripBook = mTripBookRepository.getById(domainEvent.tripBookId);
        CollectibleBook collBook = mCollectibleBookRepository.getByTripId(domainEvent.tripBookId);
        CashBook cashBook = mCashBookRepository.getByTripId(domainEvent.tripBookId);

        // -- check if exist
        try {
            Guard.NotNull(tripBook);
            Guard.NotNull(cashBook);
            Guard.NotNull(collBook);

        } catch (NullArgumentException e) {
            throw new ImpossibleState();
        }

        // Save Record
        Outcome<CashRecordService.Err.Save.CollTrans> outcome = mCashRecordService.saveRecord(
                tripBook,
                cashBook,
                collBook,
                mRecordFactory,
                domainEvent.collTransId
        );

        if (outcome.isErr()) this.handleCashRecordErr(outcome);
    }

    // region helper method ------------------------------------------------------------------------
    private void handleCashRecordErr(Outcome<CashRecordService.Err.Save.CollTrans> outcome) {
        switch (outcome.getError().orElseThrow(ImpossibleState::new)) {
            case SOURCE_TRANS_NOT_EXIST:
            case DIFFERENT_TRIP:
                throw new ImpossibleState();
            case INVALID_DATE:
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
