package com.example.domain.Collectible.domainevent;

import com.example.domain.Common.domaineventbus.DomainEvent;
import com.example.domain.Common.domaineventbus.DomainEventEnum;
import com.example.domain.Common.sharedvalueobject.id.ID;

public class CollTransAdded implements DomainEvent {

    public final ID tripBookId;
    public final ID collTransId;

    public CollTransAdded(ID tripBookId, ID collTransId) {
        this.tripBookId = tripBookId;
        this.collTransId = collTransId;
    }

    @Override
    public DomainEventEnum getEventEnum() {
        return DomainEventEnum.COLL_TRANS_ADDED;
    }
}
