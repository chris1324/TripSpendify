package com.example.data.Trip.mapper;

import com.example.data.Shared.mapper.BaseToSchemaMapper;
import com.example.data.Shared.mapper.SchemaMapper;
import com.example.data.Shared.valueObjectMapper.ValueObjectMapper;
import com.example.data.Trip.schema.tripMember.TripMemberSchema;
import com.example.data.Trip.schema.tripbook.TripBookSchema;
import com.example.domain.Shared.errorhanding.exception.ImpossibleState;
import com.example.domain.Shared.valueObject.id.ID;
import com.example.domain.Trip.tripMember.TripMember;

public class TripMemberToSchemaMapper extends SchemaMapper<
        TripMemberToSchemaMapper.Domain,
        TripMemberSchema> {

    // region Variables and Constructor ------------------------------------------------------------
    private final ValueObjectMapper mVoMapper;

    public TripMemberToSchemaMapper(ValueObjectMapper voMapper) {
        mVoMapper = voMapper;
    }
    // endregion Variables and Constructor ---------------------------------------------------------

    // region Domain -------------------------------------------------------------------------------
    public static class Domain {
        public final ID mTripBookId;
        public final TripMember mTripMember;

        public Domain(ID tripBookId, TripMember tripMember) {
            mTripBookId = tripBookId;
            mTripMember = tripMember;
        }
    }

    @Override
    public Domain mapToDomain(TripMemberSchema tripMemberSchema) {
        return new Domain(
                mVoMapper.mapID(tripMemberSchema.tripBookId),
                TripMember.create(
                                mVoMapper.mapID(tripMemberSchema.memberId),
                                mVoMapper.mapName(tripMemberSchema.name),
                                mVoMapper.mapUri(tripMemberSchema.photoUri),
                                mVoMapper.mapContactNumber(tripMemberSchema.contactNumber))
                        .getValue()
                        .orElseThrow(ImpossibleState::new)
        );
    }

    // endregion Domain ----------------------------------------------------------------------------

    @Override
    public TripMemberSchema mapToSchema(Domain domain) {
        return new TripMemberSchema(
                mVoMapper.mapID(domain.mTripMember.getId()),
                mVoMapper.mapID(domain.mTripBookId),
                mVoMapper.mapName(domain.mTripMember.getMemberName()),
                mVoMapper.mapUri(domain.mTripMember.getPhotoUri()),
                mVoMapper.mapContactNumber(domain.mTripMember.getContactNumber())
        );
    }
    // ---------------------------------------------------------------------------------------------
}
