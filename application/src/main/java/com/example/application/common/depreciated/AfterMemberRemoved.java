package com.example.application.common.depreciated;

import com.example.application.common.repository.CollectibleBookRepository;
import com.example.domain.Collectible.collectibleBook.CollectibleBook;
import com.example.domain.Collectible.collectibleBook.CollectibleTransactionService;
import com.example.domain.Collectible.collectibleTransaction.CollectibleTransaction;
import com.example.domain.Common.domaineEventBus.DomainEventEnum;
import com.example.domain.Common.domaineEventBus.DomainEventHandler;
import com.example.domain.Trip.tripEvent.MemberRemoved;

import java.util.List;


@Deprecated
public class AfterMemberRemoved extends DomainEventHandler<MemberRemoved> {
    private final CollectibleBookRepository mCollectibleBookRepository;
    private final CollectibleTransactionService mCollectibleTransactionService;

    // region Variables and Constructor ------------------------------------------------------------

    public AfterMemberRemoved(CollectibleBookRepository collectibleBookRepository,
                              CollectibleTransactionService collectibleTransactionService) {
        super(DomainEventEnum.MEMBER_REMOVED);
        mCollectibleBookRepository = collectibleBookRepository;
        mCollectibleTransactionService = collectibleTransactionService;
    }

    // endregion Variables and Constructor ---------------------------------------------------------

    @Override
    public void onEventDispatched(MemberRemoved domainEvent) {
        // Prepare
        CollectibleBook collectibleBook = mCollectibleBookRepository.getByTripBkId(domainEvent.tripId);
        List<CollectibleTransaction> allMemberTrans = collectibleBook
                .getCollectibleTrans()
                .searchReturnAll(transaction -> transaction.getInvolvedMemberId() == domainEvent.memberId);

        // Remove
        for (CollectibleTransaction collTrans : allMemberTrans) {
            mCollectibleTransactionService.removeCollTrans(collectibleBook,collTrans.getId());
        }

    }
}
