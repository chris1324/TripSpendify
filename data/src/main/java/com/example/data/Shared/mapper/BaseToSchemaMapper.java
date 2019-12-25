package com.example.data.Shared.mapper;

import java.util.List;
import java.util.stream.Collectors;

public abstract class BaseToSchemaMapper<DomainModel, Schema>  implements ToSchemaMapper<DomainModel, Schema>  {
    @Override
    public List<Schema> mapToSchema(List<DomainModel> domainModels) {
        return domainModels.stream()
                .map(this::mapToSchema)
                .collect(Collectors.toList());
    }


}
