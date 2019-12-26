package com.example.application.Trip.tripMember.dto;

import com.example.application.Shared.baseUseCase.useCase.UseCase;

import javax.annotation.Nullable;

public class TripMemberDTO implements UseCase.RequestDTO,UseCase.ResponseDTO {

    public final String memberId;
    public final String name;
    public final String photoUri;
    public final String contactNumber;

    public TripMemberDTO(@Nullable String memberId, String name, String photoUri, String contactNumber) {
        this.memberId = memberId;
        this.name = name;
        this.photoUri = photoUri;
        this.contactNumber = contactNumber;

    }
}
