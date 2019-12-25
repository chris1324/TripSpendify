package com.example.domain.Shared.entityList;

import com.example.domain.Shared.commandBaseClass.entity.Entity;
import com.example.domain.Shared.valueObject.id.ID;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

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
    public Set<ID> getRemoved() {
        return mEntityList.getRemoved();
    }

    @Override
    public List<E> getModified() {
        return mEntityList.getModified();
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
    public Optional<E> searchFirst(Function<E, Boolean> function) {
        return mEntityList.searchFirst(function);
    }

    @Override
    public List<E> searchAny(Function<E, Boolean> function) {
        return mEntityList.searchAny(function);
    }

    // ---------------------------------------------------------------------------------------------

    @Override
    public EntityList<E> unmodifiable() {
        return this;
    }
}
