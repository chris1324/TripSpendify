package com.example.domain.Trip.domainevent;

import com.example.domain.Common.domaineventbus.DomainEvent;
import com.example.domain.Common.domaineventbus.DomainEventEnum;
import com.example.domain.Common.sharedvalueobject.id.ID;

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
