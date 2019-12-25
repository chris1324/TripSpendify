package com.example.domain.Shared.entityList;

import com.example.domain.Shared.commandBaseClass.entity.Entity;
import com.example.domain.Shared.valueObject.id.ID;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

class EntityListImpl<E extends Entity> implements EntityList<E> {

    private final Map<ID, E> entityMap = new HashMap<>();
    private final Set<ID> modifiedEntityId = new HashSet<>();
    private final Set<ID> removedEntityId = new HashSet<>();

    EntityListImpl(List<E> entityList) {
        this.putToMap(entityList);
    }

    // ---------------------------------------Command-----------------------------------------------
    @Override
    public void put(E entity) {
        this.putToMap(entity);
        this.markAsModified(entity.getId());
    }

    @Override
    public void remove(ID id) {
        this.removeFromMap(id);
        this.removeFromModifiedMark(id);
        this.markAsRemoved(id);
    }

    @Override
    public void remove(Function<E, Boolean> function) {
        this.searchFirst(function).ifPresent(e -> EntityListImpl.this.remove(e.getId()));
    }

    @Override
    public void removeAll() {
        for (ID id : entityMap.keySet()) this.remove(id);
    }

    // -----------------------------------------Query-----------------------------------------------
    @Override
    public Optional<E> get(ID id) {
        E result = entityMap.get(id);
        if (result == null) return Optional.empty();
        return Optional.of(result);
    }

    @Override
    public List<E> getAll() {
        return Collections.unmodifiableList(new ArrayList<>(entityMap.values()));
    }


    @Override
    public Set<ID> getRemoved() {
        return Collections.unmodifiableSet(removedEntityId);
    }

    @Override
    public List<E> getModified() {
        List<E> list = new ArrayList<>();
        for (ID id: modifiedEntityId) list.add(entityMap.get(id));
        return list;
    }

    @Override
    public int size() {
        return entityMap.size();
    }

    @Override
    public boolean contain(Function<E, Boolean> criteria) {
        final List<E> entities = getAll();
        for (E entity : entities) {
            if (criteria.apply(entity)) return true;
        }
        return false;
    }

    @Override
    public boolean contain(ID id) {
        return entityMap.containsKey(id);
    }

    @Override
    public Optional<E> searchFirst(Function<E, Boolean> criteria) {
        final List<E> entities = getAll();
        for (E entity : entities) {
            if (criteria.apply(entity)) {
                return Optional.of(entity);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<E> searchAny(Function<E, Boolean> criteria) {
        List<E> searchResult = new ArrayList<>();
        final List<E> entities = getAll();

        for (E entity : entities) {
            if (criteria.apply(entity)) {
                searchResult.add(entity);
            }
        }
        return searchResult;
    }

    // ---------------------------------------------------------------------------------------------

    @Override
    public EntityList<E> unmodifiable() {
        return EntityList.unmodifiableList(this);
    }

    // region helper method ------------------------------------------------------------------------
    private void putToMap(E entity) {
        entityMap.put(entity.getId(), entity);
    }

    private void putToMap(List<E> entityList) {
        if (entityList != null) {
            for (E entity : entityList) {
                this.putToMap(entity);
            }
        }
    }

    private void markAsModified(ID id) {
        modifiedEntityId.add(id);
    }

    private void removeFromMap(ID id) {
        entityMap.remove(id);
    }

    private void removeFromModifiedMark(ID id) {
        modifiedEntityId.remove(id);
    }

    private void markAsRemoved(ID id) {
        removedEntityId.add(id);
    }
    // endregion helper method ---------------------------------------------------------------------

}
