package com.example.domain.Common.entityList;

import com.example.domain.Common.baseclass.entity.Entity;
import com.example.domain.Common.sharedValueObject.id.ID;
import com.example.domain.Common.function.Function;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EntityList<E extends Entity> {

    // --------------------------------------Static-------------------------------------------------
    static <E extends Entity> EntityList<E> newList(List<E> entityList) {
        return new EntityListImpl<>(entityList);
    }

    static <E extends Entity> EntityList<E> unmodifiableList(EntityList<E> entityList) {
        return new UnmodifiableEntityList<>(entityList);
    }

    enum Mark {
        Modified,
        Removed
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

    Optional<E> searchReturnFirst(Function<E, Boolean> function);

    List<E> searchReturnAll(Function<E, Boolean> function);

    List<E> getAll();

    Set<ID> getAll(Mark mark);

    int size();

    // ---------------------------------------------------------------------------------------------

    EntityList<E> unmodifiable();
}
