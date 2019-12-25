package com.example.application.Trip.dto.tripDTO;

import com.example.application.Shared.baseUseCase.useCase.UseCase;

import javax.annotation.Nullable;

public class TripDTO implements UseCase.RequestDTO, UseCase.ResponseDTO{

    public final String tripBookId;
    public final String tripName;
    public final String photoUri;
    public final long startDateMillis;
    public final long endDateMillis;

    public TripDTO(@Nullable String tripBookId, String tripName, String photoUri, long startDateMillis, long endDateMillis) {
        this.tripBookId = tripBookId;
        this.tripName = tripName;
        this.photoUri = photoUri;
        this.startDateMillis = startDateMillis;
        this.endDateMillis = endDateMillis;
    }
}
