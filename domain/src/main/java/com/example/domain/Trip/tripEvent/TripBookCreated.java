package com.example.domain.Trip.tripEvent;

import com.example.domain.Common.domaineEventBus.DomainEvent;
import com.example.domain.Common.domaineEventBus.DomainEventEnum;
import com.example.domain.Common.sharedValueObject.id.ID;

public class TripBookCreated implements DomainEvent {
    @Override
    public DomainEventEnum getEventEnum() {
        return DomainEventEnum.TRIP_BOOK_CREATE;
    }

    public final ID tripId;

    public TripBookCreated(ID tripId) {
        this.tripId = tripId;
    }

}
