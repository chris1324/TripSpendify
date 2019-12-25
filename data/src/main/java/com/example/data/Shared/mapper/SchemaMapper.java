package com.example.data.Shared.mapper;

import java.util.List;
import java.util.stream.Collectors;

public abstract class SchemaMapper<DomainModel, Schema> implements ToDomainMapper<DomainModel, Schema>, ToSchemaMapper<DomainModel, Schema> {
    @Override
    public List<Schema> mapToSchema(List<DomainModel> domainModels) {
        return domainModels.stream()
                .map(this::mapToSchema)
                .collect(Collectors.toList());
    }

    @Override
    public List<DomainModel> mapToDomain(List<Schema> schemas) {
        return schemas.stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }
}
