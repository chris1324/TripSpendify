package com.example.application.Shared.repository;

import com.example.domain.Shared.valueObject.id.ID;

public interface CommandModelRepository<T> extends Repository{

    boolean exist(ID id);

    void save(T t);

    void remove(ID id);
}
