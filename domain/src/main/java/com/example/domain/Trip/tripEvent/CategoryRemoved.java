package com.example.domain.Trip.tripEvent;

import com.example.domain.Common.domaineEventBus.DomainEvent;
import com.example.domain.Common.domaineEventBus.DomainEventEnum;
import com.example.domain.Common.sharedValueObject.id.ID;

public class CategoryRemoved implements DomainEvent {

    public final ID tripId;
    public final ID categoryId;

    public CategoryRemoved(ID tripId, ID categoryId) {
        this.tripId = tripId;
        this.categoryId = categoryId;
    }

    @Override
    public DomainEventEnum getEventEnum() {
        return null;
    }
}
