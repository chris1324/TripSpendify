package com.example.domain.Shared.commandBaseClass.aggregateroot;

import com.example.domain.Shared.commandBaseClass.entity.Entity;
import com.example.domain.Shared.domainEventBus.DomainEvent;
import com.example.domain.Shared.domainEventBus.DomainEventBus;
import com.example.domain.Shared.valueObject.id.ID;

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
