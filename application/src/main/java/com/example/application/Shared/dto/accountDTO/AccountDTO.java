package com.example.application.Shared.dto.accountDTO;

import com.example.application.Shared.baseUseCase.useCase.UseCase;

public class AccountDTO implements UseCase.ResponseDTO {

    public final String tripId;

    public AccountDTO(String tripId) {
        this.tripId = tripId;
    }
}
