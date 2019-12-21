package com.example.application.common.depreciated;

import com.example.application.common.repository.CashBookRepository;
import com.example.application.common.repository.CollectibleBookRepository;
import com.example.domain.Cash.cashBook.CashBook;
import com.example.domain.Cash.cashBook.CashRecordService;
import com.example.domain.Collectible.collectibleBook.CollectibleBook;
import com.example.domain.Collectible.collectibleEvent.CollTransRemoved;
import com.example.domain.Common.domaineEventBus.DomainEventEnum;
import com.example.domain.Common.domaineEventBus.DomainEventHandler;
import com.example.domain.Common.errorhanding.exception.ImpossibleState;
import com.example.domain.Common.errorhanding.exception.UnhandledError;
import com.example.domain.Common.errorhanding.outcome.Outcome;


@Deprecated
public class AfterCollTransRemoved extends DomainEventHandler<CollTransRemoved> {

    // region Variables and Constructor ------------------------------------------------------------
    private final CollectibleBookRepository mCollectibleBookRepository;
    private final CashBookRepository mCashBookRepository;
    private final CashRecordService mCashRecordService;

    public AfterCollTransRemoved(CollectibleBookRepository collectibleBookRepository,
                                 CashBookRepository cashBookRepository,
                                 CashRecordService cashRecordService) {
        super(DomainEventEnum.COLL_TRANS_REMOVED);
        mCollectibleBookRepository = collectibleBookRepository;
        mCashBookRepository = cashBookRepository;
        mCashRecordService = cashRecordService;
    }

    // endregion Variables and Constructor ---------------------------------------------------------

    @Override
    public void onEventDispatched(CollTransRemoved domainEvent) {
        CollectibleBook collectibleBook = mCollectibleBookRepository.getByTripBkId(domainEvent.tripBookId);
        CashBook cashBook = mCashBookRepository.getByTripBkId(domainEvent.tripBookId);

        Outcome<CashRecordService.Err.Remove> outcome = mCashRecordService.removeRecord(
                collectibleBook,
                cashBook,
                domainEvent.collTransId);

        if (outcome.isErr()) this.handleError(outcome);
    }

    // region helper method ------------------------------------------------------------------------
    private void handleError(Outcome<CashRecordService.Err.Remove> outcome) {
        switch (outcome.getError().orElseThrow(ImpossibleState::new)) {
            case DIFFERENT_TRIP:
            case SOURCE_STILL_EXIST:
                throw new ImpossibleState();
            default:
                throw new UnhandledError();
        }
    }
    // endregion helper method ---------------------------------------------------------------------
}
