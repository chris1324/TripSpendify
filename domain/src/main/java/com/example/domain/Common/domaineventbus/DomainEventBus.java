package com.example.domain.Common.domaineventbus;

import com.example.domain.Common.baseclass.aggregateroot.AggregateRoot;
import com.example.domain.Common.sharedvalueobject.id.ID;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DomainEventBus {

    // --------------------------------------Static-------------------------------------------------
    public static DomainEventBus instance;

    public static DomainEventBus getInstance() {
        if (instance == null) instance = new DomainEventBus();
        return instance;
    }

    protected DomainEventBus() {
    }

    // --------------------------------------Non static---------------------------------------------

    private final Map<DomainEventEnum, List<DomainEventHandler>> mEventHandlers = new HashMap<>();
    private final Map<ID, AggregateRoot> mMarkedAggregates = new HashMap<>();

    public void markAggregate(AggregateRoot aggregateRoot) {
        mMarkedAggregates.put(aggregateRoot.getId(), aggregateRoot);
    }

    public void dispatch(ID aggregateId) {
        AggregateRoot aggregateRoot = mMarkedAggregates.get(aggregateId);
        if (aggregateId == null) return;

        List<DomainEvent> domainEvent = aggregateRoot.getDomainEvent();
        for (DomainEvent event : domainEvent) dispatchToHandler(event);
        removeAggregateFromMarked(aggregateId);
        aggregateRoot.clearEvent();
    }

    public void registerHandler(DomainEventEnum eventEnum, DomainEventHandler handler) {
        boolean isEventNumExist = mEventHandlers.containsKey(eventEnum);
        if (!isEventNumExist) mEventHandlers.put(eventEnum, new ArrayList<>());
        mEventHandlers.get(eventEnum).add(handler);
    }

    public void clearHandlers() {
        mEventHandlers.clear();
    }

    public void clearMarkedAggregates() {
        mMarkedAggregates.clear();
    }


    // ---------------------------------------------------------------------------------------------

    // region helper method ------------------------------------------------------------------------
    private void dispatchToHandler(DomainEvent domainEvent) {
        List<DomainEventHandler> handlers = mEventHandlers.get(domainEvent.getEventEnum());
        for (DomainEventHandler handler : handlers) handler.onEventDispatched(domainEvent);
    }

    private void removeAggregateFromMarked(ID aggregateRootId) {
        mMarkedAggregates.remove(aggregateRootId);
    }

    // endregion helper method ---------------------------------------------------------------------
}
