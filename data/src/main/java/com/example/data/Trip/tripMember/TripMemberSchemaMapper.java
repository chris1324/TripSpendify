package com.example.data.Trip.tripMember;

import com.example.data.Shared.mapper.SchemaMapper;
import com.example.data.Shared.valueObjectMapper.ValueObjectMapper;
import com.example.domain.Shared.errorhanding.exception.ImpossibleState;
import com.example.domain.Shared.valueObject.id.ID;
import com.example.domain.Trip.tripMember.TripMember;

public class TripMemberSchemaMapper extends SchemaMapper<TripMemberSchemaMapper.Domain,TripMemberSchema> {

    // region Variables and Constructor ------------------------------------------------------------

    public TripMemberSchemaMapper(ValueObjectMapper voMapper) {
        super(voMapper);
    }

    // endregion Variables and Constructor ---------------------------------------------------------

    // region Domain -------------------------------------------------------------------------------
    public static class Domain {
        public final ID mTripBookId;
        public final TripMember mTripMember;

        public Domain(ID tripBookId, TripMember tripMember) {
            this.mTripBookId = tripBookId;
            mTripMember = tripMember;
        }
    }
    // endregion Domain ----------------------------------------------------------------------------


    @Override
    public TripMemberSchema mapToSchema(Domain domain) {
        return new TripMemberSchema(
                this.mapID(domain.mTripMember.getId()),
                this.mapID(domain.mTripBookId),
                this.mapName(domain.mTripMember.getMemberName()),
                this.mapUri(domain.mTripMember.getPhotoUri()),
                this.mapContactNumber(domain.mTripMember.getContactNumber()));
    }

    @Override
    public Domain mapToDomain(TripMemberSchema tripMemberSchema) {
        return new Domain(
                this.mapID(tripMemberSchema.tripBookId),
                TripMember.create(
                        this.mapID(tripMemberSchema.memberId),
                        this.mapName(tripMemberSchema.name),
                        this.mapUri(tripMemberSchema.photoUri),
                        this.mapContactNumber(tripMemberSchema.contactNumber))
                        .getValue()
                        .orElseThrow(ImpossibleState::new));
    }
}
