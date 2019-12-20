package com.example.domain.Trip.domainevent;

import com.example.domain.Common.domaineventbus.DomainEvent;
import com.example.domain.Common.domaineventbus.DomainEventEnum;
import com.example.domain.Common.sharedvalueobject.id.ID;

public class TripExpenseAdded implements DomainEvent {

    public final ID tripBookId;
    public final ID tripExpenseID;

    public TripExpenseAdded(ID tripBookId, ID tripExpenseID) {
        this.tripBookId = tripBookId;
        this.tripExpenseID = tripExpenseID;
    }


    @Override
    public DomainEventEnum getEventEnum() {
        return DomainEventEnum.TRIP_EXPENSE_ADDED;
    }
}
