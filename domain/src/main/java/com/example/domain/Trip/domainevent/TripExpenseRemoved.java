package com.example.domain.Trip.domainevent;

import com.example.domain.Common.domaineventbus.DomainEvent;
import com.example.domain.Common.domaineventbus.DomainEventEnum;
import com.example.domain.Common.sharedvalueobject.id.ID;

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
