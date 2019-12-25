package com.example.domain.Shared.domainEventBus;

public abstract class DomainEventHandler<E extends DomainEvent> {

    public DomainEventHandler(DomainEventEnum domainEventEnum) {
        DomainEventBus.getInstance().registerHandler(domainEventEnum, this);
    }

    abstract public void onEventDispatched(E domainEvent);

}
