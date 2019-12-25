package com.example.application.Shared.dtoMapper;

public interface DtoMapper<DomainModel, Dto> {

    Dto mapToDto(DomainModel domainModel);
}
