package com.example.domain.Shared.entityList;

import com.example.domain.Shared.commandBaseClass.entity.Entity;
import com.example.domain.Shared.valueObject.id.ID;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

public interface EntityList<E extends Entity> {

    // --------------------------------------Static-------------------------------------------------
    static <E extends Entity> EntityList<E> newList(List<E> entityList) {
        return new EntityListImpl<>(entityList);
    }

    static <E extends Entity> EntityList<E> unmodifiableList(EntityList<E> entityList) {
        return new UnmodifiableEntityList<>(entityList);
    }

    // ---------------------------------------Command-----------------------------------------------
    void put(E entity);

    void remove(ID id);

    void remove(Function<E, Boolean> function);

    void removeAll();

    // -----------------------------------------Query-----------------------------------------------
    Optional<E> get(ID id);

    boolean contain(Function<E, Boolean> function);

    boolean contain(ID id);

    Optional<E> searchFirst(Function<E, Boolean> function);

    List<E> searchAny(Function<E, Boolean> function);

    List<E> getAll();

    Set<ID> getRemoved();

    List<E> getModified();

    int size();

    // ---------------------------------------------------------------------------------------------

    EntityList<E> unmodifiable();
}
