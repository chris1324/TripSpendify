package com.example.application.Shared.baseUseCase.useCase;

public abstract class UseCase<Q extends UseCase.RequestDTO, R extends UseCase.ResponseDTO> {

    public interface RequestDTO {
    }

    public interface ResponseDTO {
    }

    public interface Void extends RequestDTO, ResponseDTO {
    }
}
