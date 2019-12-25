package com.example.data.Shared.mapper;

import java.util.List;
import java.util.stream.Collectors;

public interface ToSchemaMapper<DomainModel, Schema> {

    Schema mapToSchema(DomainModel domainModel);

    List<Schema> mapToSchema(List<DomainModel> domainModels);
}
