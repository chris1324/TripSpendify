package com.example.domain.Common.domainevent;

import com.example.domain.Common.baseclass.aggregateroot.AggregateRoot;
import com.example.domain.Common.sharedvalueobject.id.ID;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DomainEventBus {
    private static final Map<DomainEventEnum, List<DomainEventHandler>> mEventHandlers = new HashMap<>();
    private static final Map<ID, AggregateRoot> mMarkedAggregates = new HashMap<>();

    public static void markAggregate(AggregateRoot aggregateRoot) {
        mMarkedAggregates.put(aggregateRoot.getId(), aggregateRoot);
    }

    public static void dispatch(ID aggregateId) {
        AggregateRoot aggregateRoot = mMarkedAggregates.get(aggregateId);
        if (aggregateId == null) return;

        List<DomainEvent> domainEvent = aggregateRoot.getDomainEvent();
        for (DomainEvent event : domainEvent) dispatchToHandler(event);
        removeAggregateFromMarked(aggregateId);
        aggregateRoot.clearEvent();
    }

    public static void registerHandler(DomainEventEnum eventEnum, DomainEventHandler handler) {
        boolean isEventNumExist = mEventHandlers.containsKey(eventEnum);
        if (!isEventNumExist) mEventHandlers.put(eventEnum, new ArrayList<>());
        mEventHandlers.get(eventEnum).add(handler);
    }

    public static void clearHandlers() {
        mEventHandlers.clear();
    }

    public static void clearMarkedAggregates() {
        mMarkedAggregates.clear();
    }


    // ---------------------------------------------------------------------------------------------

    // region helper method ------------------------------------------------------------------------
    private static void dispatchToHandler(DomainEvent domainEvent) {
        List<DomainEventHandler> handlers = mEventHandlers.get(domainEvent.getEventEnum());
        for (DomainEventHandler handler : handlers) handler.onEventDispatched(domainEvent);
    }

    private static void removeAggregateFromMarked(ID aggregateRootId) {
        mMarkedAggregates.remove(aggregateRootId);
    }

    // endregion helper method ---------------------------------------------------------------------
}
