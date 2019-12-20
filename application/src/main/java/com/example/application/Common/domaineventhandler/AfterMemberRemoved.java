package com.example.application.Common.domaineventhandler;

import com.example.application.Common.repository.CollectibleBookRepository;
import com.example.domain.Collectible.collectiblebook.CollectibleBook;
import com.example.domain.Collectible.collectiblebook.CollectibleTransactionService;
import com.example.domain.Collectible.collectibletransaction.CollectibleTransaction;
import com.example.domain.Common.domaineventbus.DomainEventEnum;
import com.example.domain.Common.domaineventbus.DomainEventHandler;
import com.example.domain.Trip.domainevent.MemberRemoved;

import java.util.List;
import java.util.Optional;

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
        CollectibleBook collectibleBook = mCollectibleBookRepository.getByTripBkId(domainEvent.tripId);
        List<CollectibleTransaction> memberTrans = collectibleBook
                .getCollectibleTrans()
                .searchReturnAll(transaction -> transaction.getInvolvedMemberId() == domainEvent.memberId);

        if (memberTrans.isPresent()){
            final CollectibleTransaction collectibleTransaction = memberTrans.get();
        }
    }
}
