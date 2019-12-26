package com.example.data.Shared.repository;

import com.example.data.Shared.valueObjectMapper.ValueObjectMapper;
import com.example.data.Trip.tripbook.TripBookSchemaMapper;
import com.example.domain.Shared.domainEventBus.DomainEventBus;
import com.example.domain.Shared.valueObject.id.ID;
import com.example.domain.Trip.tripBook.TripBook;

public abstract class BaseRepository {

    protected final ValueObjectMapper mVoMapper;

    public BaseRepository(ValueObjectMapper voMapper) {
        mVoMapper = voMapper;
    }

    // region helper method ------------------------------------------------------------------------
    protected String mapID(ID id){
        return  mVoMapper.mapID(id);
    }

    protected ID mapID(String id){
        return mVoMapper.mapID(id);
    }
    // endregion helper method ---------------------------------------------------------------------
}
