package com.example.data.Shared.repository;

import androidx.annotation.CallSuper;

import com.example.data.Shared.valueObjectMapper.ValueObjectMapper;
import com.example.domain.Shared.commandBaseClass.aggregateroot.AggregateRoot;
import com.example.domain.Shared.domainEventBus.DomainEventBus;
import com.example.domain.Shared.valueObject.id.ID;

import javax.annotation.OverridingMethodsMustInvokeSuper;

public abstract class AggregateRootRepository<T extends AggregateRoot> extends BaseRepository {

    public AggregateRootRepository(ValueObjectMapper voMapper) {
        super(voMapper);
    }

    @OverridingMethodsMustInvokeSuper
    public void save(T t) {
        DomainEventBus.getInstance().dispatch(t.getId());
    }

    @OverridingMethodsMustInvokeSuper
    public void remove(ID id) {
        DomainEventBus.getInstance().dispatch(id);
    }
}
