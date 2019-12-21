package com.example.domain.Collectible.collectibleEvent;

import com.example.domain.Common.domaineEventBus.DomainEvent;
import com.example.domain.Common.domaineEventBus.DomainEventEnum;
import com.example.domain.Common.sharedValueObject.id.ID;

public class CollTransRemoved implements DomainEvent {

    public final ID tripBookId;
    public final ID collTransId;

    public CollTransRemoved(ID tripBookId, ID collTransId) {
        this.tripBookId = tripBookId;
        this.collTransId = collTransId;
    }

    @Override
    public DomainEventEnum getEventEnum() {
        return DomainEventEnum.COLL_TRANS_REMOVED;
    }
}
