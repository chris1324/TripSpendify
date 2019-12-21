package com.example.domain.Common.baseclass.aggregateroot;

import com.example.domain.Common.domaineEventBus.DomainEvent;
import com.example.domain.Common.baseclass.entity.Entity;
import com.example.domain.Common.domaineEventBus.DomainEventBus;
import com.example.domain.Common.sharedValueObject.id.ID;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AggregateRoot extends Entity {

    private final List<DomainEvent> mDomainEvent = new ArrayList<>();

    public AggregateRoot(ID id) {
        super(id);
    }

    protected void addDomainEvent(DomainEvent domainEvent){
        mDomainEvent.add(domainEvent);
        DomainEventBus.getInstance().markAggregate(this);
    }

    public void clearEvent(){
        mDomainEvent.clear();
    }

    public List<DomainEvent> getDomainEvent() {
        return Collections.unmodifiableList(mDomainEvent);
    }
}
