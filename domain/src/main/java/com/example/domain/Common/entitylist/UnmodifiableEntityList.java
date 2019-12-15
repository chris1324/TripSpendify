package com.example.domain.Common.entitylist;

import com.example.domain.Common.entity.Entity;
import com.example.domain.Common.entity.ID;
import com.example.domain.Common.function.Function;

import java.util.List;

class UnmodifiableEntityList<Type extends Entity> implements EntityList<Type> {

    private final EntityList<Type> mEntityList;

    UnmodifiableEntityList(EntityList<Type> entityList) {
        mEntityList = entityList;
    }

    // ---------------------------------------Command-----------------------------------------------
    @Override
    public void put(Type entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void remove(ID id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeAll() {
        throw new UnsupportedOperationException();
    }

    // -----------------------------------------Query-----------------------------------------------
    @Override
    public Type get(ID id) {
        return mEntityList.get(id);
    }

    @Override
    public List<Type> getAll() {
        return mEntityList.getAll();
    }

    @Override
    public List<ID> getAll(Mark mark) {
        return mEntityList.getAll(mark);
    }

    @Override
    public int size() {
        return mEntityList.size();
    }

    @Override
    public boolean contain(Function<Type, Boolean> function) {
        return mEntityList.contain(function);
    }

    @Override
    public boolean contain(ID id) {
        return mEntityList.contain(id);
    }
}
