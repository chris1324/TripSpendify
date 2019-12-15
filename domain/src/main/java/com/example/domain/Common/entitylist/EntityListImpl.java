package com.example.domain.Common.entitylist;

import com.example.domain.Common.entity.Entity;
import com.example.domain.Common.entity.ID;
import com.example.domain.Common.function.Function;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class EntityListImpl<E extends Entity> implements EntityList<E> {

    private final Map<ID, E> entityMap = new HashMap<>();
    private final List<ID> modifiedEntityId = new ArrayList<>();
    private final List<ID> removedEntityId = new ArrayList<>();

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
    public void removeAll() {
        for (ID id : entityMap.keySet()) remove(id);
    }

    // -----------------------------------------Query-----------------------------------------------
    @Override
    public E get(ID id) {
        return entityMap.get(id);
    }

    @Override
    public List<E> getAll() {
        return Collections.unmodifiableList(new ArrayList<>(entityMap.values()));
    }

    @Override
    public List<ID> getAll(Mark mark) {
        if (mark == Mark.Modified)
            return Collections.unmodifiableList(modifiedEntityId);

        if (mark == Mark.Removed)
            return Collections.unmodifiableList(removedEntityId);

        return new ArrayList<>();
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
