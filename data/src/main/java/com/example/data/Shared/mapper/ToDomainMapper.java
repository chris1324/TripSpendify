package com.example.data.Shared.mapper;

import java.util.List;
import java.util.stream.Collectors;

public interface ToDomainMapper<DomainModel, Schema> {

      DomainModel mapToDomain(Schema schema);

      List<DomainModel> mapToDomain(List<Schema> schemas);


}
