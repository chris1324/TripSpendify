package com.example.data.Trip.mapper;

import com.example.data.Shared.mapper.BaseToSchemaMapper;
import com.example.data.Shared.valueObjectMapper.ValueObjectMapper;
import com.example.data.Trip.schema.tripbook.TripBookSchema;
import com.example.domain.Trip.tripBook.TripBook;

public class TripBookToSchemaMapper extends BaseToSchemaMapper<TripBook, TripBookSchema> {

    // region Variables and Constructor ------------------------------------------------------------
    private final ValueObjectMapper mVoMapper;

    public TripBookToSchemaMapper(ValueObjectMapper voMapper) {
        mVoMapper = voMapper;
    }

    // endregion Variables and Constructor ---------------------------------------------------------

    @Override
    public TripBookSchema mapToSchema(TripBook tripBook) {
        return new TripBookSchema(
                mVoMapper.mapID(tripBook.getId()),
                mVoMapper.mapName(tripBook.getTripName()),
                mVoMapper.mapUri(tripBook.getPhotoUri()),
                mVoMapper.mapDate(tripBook.getStartDate()),
                mVoMapper.mapDate(tripBook.getEndDate())
        );
    }

    // region helper method ------------------------------------------------------------------------

    // endregion helper method ---------------------------------------------------------------------

}
