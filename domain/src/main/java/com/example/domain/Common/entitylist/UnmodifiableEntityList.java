package com.example.domain.Common.entitylist;

import com.example.domain.Common.entity.Entity;
import com.example.domain.Common.entity.ID;
import com.example.domain.Common.function.Function;

import java.util.List;
import java.util.Optional;

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
    public List<ID> getAll(Mark mark) {
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
    public Optional<E> search(Function<E, Boolean> function) {
        return mEntityList.search(function);
    }


    // ---------------------------------------------------------------------------------------------

    @Override
    public EntityList<E> unmodifiable() {
        return this;
    }
}
