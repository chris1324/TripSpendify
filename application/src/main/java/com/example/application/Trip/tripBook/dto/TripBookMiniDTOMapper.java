package com.example.application.Trip.tripBook.dto;

import com.example.application.Shared.dtoMapper.DtoMapper;
import com.example.application.Trip.tripBook.minimal.TripBookMinimal;

public class TripBookMiniDTOMapper implements DtoMapper<TripBookMinimal, TripBookMiniDTO> {

    @Override
    public TripBookMiniDTO mapToDto(TripBookMinimal tripMinimal) {
        return new TripBookMiniDTO(
                tripMinimal.tripBookId.toString(),
                tripMinimal.mTripName.toString(),
                tripMinimal.mPhotoUri.toString(),
                tripMinimal.mStartDate.getMillisecond(),
                tripMinimal.mEndDate.getMillisecond()
        );
    }
}
