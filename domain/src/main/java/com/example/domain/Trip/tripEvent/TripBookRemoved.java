package com.example.domain.Trip.tripEvent;

import com.example.domain.Common.domaineEventBus.DomainEvent;
import com.example.domain.Common.domaineEventBus.DomainEventEnum;
import com.example.domain.Common.sharedValueObject.id.ID;

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
