package com.example.data.Trip.repository;

import com.example.application.Shared.repository.Repository;
import com.example.data.Shared.valueObjectMapper.ValueObjectMapper;
import com.example.data.Trip.dao.TripMemberDAO;
import com.example.data.Trip.mapper.TripMemberToSchemaMapper;
import com.example.domain.Shared.entityList.EntityList;
import com.example.domain.Shared.valueObject.id.ID;
import com.example.domain.Trip.tripMember.TripMember;

import java.util.stream.Collectors;

public class TripMemberRepo{

    // region Variables and Constructor ------------------------------------------------------------
    private final TripMemberDAO mTripMemberDAO;
    private final TripMemberToSchemaMapper mMapper;
    private final ValueObjectMapper mVoMapper;

    public TripMemberRepo(TripMemberDAO tripMemberDAO, TripMemberToSchemaMapper mapper, ValueObjectMapper voMapper) {
        mTripMemberDAO = tripMemberDAO;
        mMapper = mapper;
        mVoMapper = voMapper;
    }

    // endregion Variables and Constructor ---------------------------------------------------------

    // ---------------------------------------- Command --------------------------------------------
    public void save(ID tripBookId, EntityList<TripMember> tripMembers) {
        mTripMemberDAO.delete(tripMembers
                .getRemoved().stream()
                .map(mVoMapper::mapID)
                .collect(Collectors.toList()));

        mTripMemberDAO.upsert(tripMembers
                .getModified().stream()
                .map(tripMember -> mMapper.mapToSchema(new TripMemberToSchemaMapper.Domain(tripBookId, tripMember)))
                .collect(Collectors.toList()));
    }

    // ---------------------------------------------------------------------------------------------
}
