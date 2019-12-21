package com.example.application.common.repository;

import com.example.domain.Common.sharedValueObject.id.ID;

public interface Repository<T> {

    boolean exist(ID id);

    void save(T t);

    void remove(ID id);
}
