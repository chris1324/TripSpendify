package com.example.domain.Trip.tripEvent;

import com.example.domain.Common.domaineEventBus.DomainEvent;
import com.example.domain.Common.domaineEventBus.DomainEventEnum;
import com.example.domain.Common.sharedValueObject.id.ID;

public class MemberRemoved implements DomainEvent {

    public final ID tripId;
    public final ID memberId;

    public MemberRemoved(ID tripId, ID memberId) {
        this.tripId = tripId;
        this.memberId = memberId;
    }

    @Override
    public DomainEventEnum getEventEnum() {
        return DomainEventEnum.MEMBER_REMOVED;
    }
}
