package com.example.application.Trip.dto.tripMemberDTO;

import com.example.application.Shared.dtoMapper.DtoMapper;
import com.example.domain.Trip.tripMember.TripMember;

public class TripMemberDTOMapper implements DtoMapper<TripMember, TripMemberDTO> {
    @Override
    public TripMemberDTO mapToDto(TripMember tripMember) {
        return new TripMemberDTO(
                tripMember.getId().toString(),
                tripMember.getMemberName().toString(),
                tripMember.getPhotoUri().toString(),
                tripMember.getContactNumber().toString());
    }
}
