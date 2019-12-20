package com.example.application.Common.domaineventhandler;

import com.example.application.Common.repository.CashBookRepository;
import com.example.application.Common.repository.CollectibleBookRepository;
import com.example.application.Common.repository.TripBookRepository;
import com.example.domain.Cash.cashbook.CashBook;
import com.example.domain.Cash.cashbook.CashRecordFactory;
import com.example.domain.Cash.cashbook.CashRecordService;
import com.example.domain.Collectible.collectiblebook.CollectibleBook;
import com.example.domain.Collectible.domainevent.CollTransAdded;
import com.example.domain.Common.domaineventbus.DomainEventEnum;
import com.example.domain.Common.domaineventbus.DomainEventHandler;
import com.example.domain.Common.errorhanding.exception.ImpossibleState;
import com.example.domain.Common.errorhanding.exception.NullArgumentException;
import com.example.domain.Common.errorhanding.exception.UnhandledError;
import com.example.domain.Common.errorhanding.guard.Guard;
import com.example.domain.Common.errorhanding.outcome.Outcome;
import com.example.domain.Trip.tripbook.TripBook;

public class AfterCollTransAdded extends DomainEventHandler<CollTransAdded> {

    // region Variables and Constructor ------------------------------------------------------------

    private final TripBookRepository mTripBookRepository;
    private final CollectibleBookRepository mCollectibleBookRepository;
    private final CashBookRepository mCashBookRepository;

    private final CashRecordService mCashRecordService;
    private final CashRecordFactory mRecordFactory;

    public AfterCollTransAdded(TripBookRepository tripBookRepository,
                               CollectibleBookRepository collectibleBookRepository,
                               CashBookRepository cashBookRepository,
                               CashRecordService cashRecordService,
                               CashRecordFactory recordFactory) {
        super(DomainEventEnum.COLL_TRANS_ADDED);
        mTripBookRepository = tripBookRepository;
        mCollectibleBookRepository = collectibleBookRepository;
        mCashBookRepository = cashBookRepository;
        mCashRecordService = cashRecordService;
        mRecordFactory = recordFactory;
    }
    // endregion Variables and Constructor ---------------------------------------------------------

    @Override
    public void onEventDispatched(CollTransAdded domainEvent) {
        // Prepare
        // -- get from repo
        TripBook tripBook = mTripBookRepository.getTripBookById(domainEvent.tripBookId);
        CollectibleBook collBook = mCollectibleBookRepository.getByTripBkId(domainEvent.tripBookId);
        CashBook cashBook = mCashBookRepository.getByTripBkId(domainEvent.tripBookId);

        // -- check if exist
        try {
            Guard.NotNull(tripBook);
            Guard.NotNull(cashBook);
            Guard.NotNull(collBook);

        } catch (NullArgumentException e) {
            throw new ImpossibleState();
        }

        // Add Record
        Outcome<CashRecordService.Err.Add.CollTrans> outcome = mCashRecordService.addRecord(
                tripBook,
                cashBook,
                collBook,
                mRecordFactory,
                domainEvent.collTransId
        );

        if (outcome.isErr()) this.handleCashRecordErr(outcome);
    }

    // region helper method ------------------------------------------------------------------------
    private void handleCashRecordErr(Outcome<CashRecordService.Err.Add.CollTrans> outcome) {
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
