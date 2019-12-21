package com.example.domain.Trip.tripEvent;

import com.example.domain.Common.domaineEventBus.DomainEvent;
import com.example.domain.Common.domaineEventBus.DomainEventEnum;
import com.example.domain.Common.sharedValueObject.id.ID;

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
