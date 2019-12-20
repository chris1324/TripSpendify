package com.example.domain.Trip.domainevent;

import com.example.domain.Common.domaineventbus.DomainEvent;
import com.example.domain.Common.domaineventbus.DomainEventEnum;
import com.example.domain.Common.sharedvalueobject.id.ID;

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
