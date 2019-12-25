package com.example.domain.Trip.tripEvent;

import com.example.domain.Shared.domainEventBus.DomainEvent;
import com.example.domain.Shared.domainEventBus.DomainEventEnum;
import com.example.domain.Shared.valueObject.id.ID;

public class TripExpenseSaved implements DomainEvent {

    public final ID tripBookId;
    public final ID tripExpenseID;

    public TripExpenseSaved(ID tripBookId, ID tripExpenseID) {
        this.tripBookId = tripBookId;
        this.tripExpenseID = tripExpenseID;
    }


    @Override
    public DomainEventEnum getEventEnum() {
        return DomainEventEnum.TRIP_EXPENSE_SAVED;
    }
}
