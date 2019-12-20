package com.example.application.Common.repository;

import com.example.domain.Common.sharedvalueobject.id.ID;

public interface Repository<T> {

    boolean exist(ID id);

    void save(T t);

    void remove(ID id);
}
