package com.example.data.Shared.mapper;

import java.util.List;
import java.util.stream.Collectors;

public abstract class BaseToDomainMapper<DomainModel, Schema>  implements ToDomainMapper<DomainModel, Schema>  {

    @Override
    public List<DomainModel> mapToDomain(List<Schema> schemas) {
        return schemas.stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }
}
