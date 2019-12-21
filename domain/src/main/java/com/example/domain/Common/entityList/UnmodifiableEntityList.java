package com.example.domain.Common.entityList;

import com.example.domain.Common.baseclass.entity.Entity;
import com.example.domain.Common.sharedValueObject.id.ID;
import com.example.domain.Common.function.Function;

import java.util.List;
import java.util.Optional;
import java.util.Set;

class UnmodifiableEntityList<E extends Entity> implements EntityList<E> {

    private final EntityList<E> mEntityList;

    UnmodifiableEntityList(EntityList<E> entityList) {
        mEntityList = entityList;
    }

    // ---------------------------------------Command-----------------------------------------------
    @Override
    public void put(E entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void remove(ID id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void remove(Function<E, Boolean> function) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeAll() {
        throw new UnsupportedOperationException();
    }


    // -----------------------------------------Query-----------------------------------------------
    @Override
    public Optional<E> get(ID id) {
        return mEntityList.get(id);
    }

    @Override
    public List<E> getAll() {
        return mEntityList.getAll();
    }

    @Override
    public Set<ID> getAll(Mark mark) {
        return mEntityList.getAll(mark);
    }

    @Override
    public int size() {
        return mEntityList.size();
    }

    @Override
    public boolean contain(Function<E, Boolean> function) {
        return mEntityList.contain(function);
    }

    @Override
    public boolean contain(ID id) {
        return mEntityList.contain(id);
    }

    @Override
    public Optional<E> searchReturnFirst(Function<E, Boolean> function) {
        return mEntityList.searchReturnFirst(function);
    }

    @Override
    public List<E> searchReturnAll(Function<E, Boolean> function) {
        return mEntityList.searchReturnAll(function);
    }

    // ---------------------------------------------------------------------------------------------

    @Override
    public EntityList<E> unmodifiable() {
        return this;
    }
}