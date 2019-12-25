package com.example.application.Trip.dto.tripDTO;

import com.example.application.Shared.dtoMapper.DtoMapper;
import com.example.domain.Trip.tripBook.TripBook;

public class TripDTOMapper implements DtoMapper<TripBook, TripDTO> {

    @Override
    public TripDTO mapToDto(TripBook tripBook) {
        return new TripDTO(
                tripBook.getId().toString(),
                tripBook.getTripName().toString(),
                tripBook.getPhotoUri().toString(),
                tripBook.getStartDate().getMillisecond(),
                tripBook.getEndDate().getMillisecond()
        );
    }
}
