package com.example.domain.Trip.domainevent;

import com.example.domain.Common.domaineventbus.DomainEvent;
import com.example.domain.Common.domaineventbus.DomainEventEnum;
import com.example.domain.Common.sharedvalueobject.id.ID;

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
