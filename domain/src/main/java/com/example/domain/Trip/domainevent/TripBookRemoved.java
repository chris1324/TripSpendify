package com.example.domain.Trip.domainevent;

import com.example.domain.Common.domaineventbus.DomainEvent;
import com.example.domain.Common.domaineventbus.DomainEventEnum;
import com.example.domain.Common.sharedvalueobject.id.ID;

public class TripBookRemoved implements DomainEvent {

    // FIXME: 20/12/2019 Need to be dispatched by Use case
    @Override
    public DomainEventEnum getEventEnum() {
        return DomainEventEnum.TRIP_BOOK_REMOVED;
    }

    public final ID tripId;

    public TripBookRemoved(ID tripId) {
        this.tripId = tripId;
    }
}
