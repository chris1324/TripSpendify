package com.example.domain.Trip.tripEvent;

import com.example.domain.Shared.domainEventBus.DomainEvent;
import com.example.domain.Shared.domainEventBus.DomainEventEnum;
import com.example.domain.Shared.valueObject.id.ID;

public class TripBookSaved implements DomainEvent {
    @Override
    public DomainEventEnum getEventEnum() {
        return DomainEventEnum.TRIP_BOOK_SAVED;
    }

    public final ID tripId;

    public TripBookSaved(ID tripId) {
        this.tripId = tripId;
    }

}
