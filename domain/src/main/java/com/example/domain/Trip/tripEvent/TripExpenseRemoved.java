package com.example.domain.Trip.tripEvent;

import com.example.domain.Common.domaineEventBus.DomainEvent;
import com.example.domain.Common.domaineEventBus.DomainEventEnum;
import com.example.domain.Common.sharedValueObject.id.ID;

public class TripExpenseRemoved implements DomainEvent {

    public final ID tripBookId;
    public final ID tripExpenseID;

    public TripExpenseRemoved(ID tripBookId, ID tripExpenseID) {
        this.tripBookId = tripBookId;
        this.tripExpenseID = tripExpenseID;
    }

    @Override
    public DomainEventEnum getEventEnum() {
        return DomainEventEnum.TRIP_BOOK_REMOVED;
    }

}
