package com.example.data.Trip.tripMember;

import com.example.data.Shared.repository.BaseRepository;
import com.example.data.Shared.valueObjectMapper.ValueObjectMapper;
import com.example.domain.Shared.entityList.EntityList;
import com.example.domain.Shared.valueObject.id.ID;
import com.example.domain.Trip.tripMember.TripMember;

import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.Observable;

public class TripMemberRepo extends BaseRepository {

    // region Variables and Constructor ------------------------------------------------------------
    private final TripMemberDao mTripMemberDao;
    private final TripMemberSchemaMapper mSchemaMapper;

    public TripMemberRepo(ValueObjectMapper valueObjectMapper, TripMemberDao tripMemberDao, TripMemberSchemaMapper schemaMapper) {
        super(valueObjectMapper);
        mTripMemberDao = tripMemberDao;
        mSchemaMapper = schemaMapper;
    }

    // endregion Variables and Constructor ---------------------------------------------------------

    // ---------------------------------------- Command --------------------------------------------
    public void save(ID tripBookId, EntityList<TripMember> tripMembers) {
        mTripMemberDao.delete(tripMembers
                .getRemoved().stream()
                .map(this::mapID)
                .collect(Collectors.toList()));

        mTripMemberDao.upsert(tripMembers
                .getModified().stream()
                .map(tripMember -> mSchemaMapper.mapToSchema(new TripMemberSchemaMapper.Domain(tripBookId, tripMember)))
                .collect(Collectors.toList()));

    }

    // ---------------------------------------- Query ----------------------------------------------
    public EntityList<TripMember> getByTripId(ID tripBookId) {
        List<TripMemberSchema> schemas = mTripMemberDao.getByTripId(this.mapID(tripBookId));
        return this.mapSchemasToEntityList(schemas);
    }

    public Observable<EntityList<TripMember>> fetchByTripId(ID tripBookId) {
        return mTripMemberDao
                .fetchByTripId(this.mapID(tripBookId))
                .map(this::mapSchemasToEntityList);
    }

    // region helper method ------------------------------------------------------------------------
    private EntityList<TripMember> mapSchemasToEntityList(List<TripMemberSchema> tripMemberSchemas) {
        return EntityList.newList(mSchemaMapper
                .mapToDomain(tripMemberSchemas)
                .stream()
                .map(domain -> domain.mTripMember)
                .collect(Collectors.toList()));
    }
    // endregion helper method ---------------------------------------------------------------------
}
