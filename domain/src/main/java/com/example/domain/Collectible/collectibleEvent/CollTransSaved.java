package com.example.domain.Collectible.collectibleEvent;

import com.example.domain.Shared.domainEventBus.DomainEvent;
import com.example.domain.Shared.domainEventBus.DomainEventEnum;
import com.example.domain.Shared.valueObject.id.ID;

public class CollTransSaved implements DomainEvent {

    public final ID tripBookId;
    public final ID collTransId;

    public CollTransSaved(ID tripBookId, ID collTransId) {
        this.tripBookId = tripBookId;
        this.collTransId = collTransId;
    }

    @Override
    public DomainEventEnum getEventEnum() {
        return DomainEventEnum.COLL_TRANS_SAVED;
    }
}
