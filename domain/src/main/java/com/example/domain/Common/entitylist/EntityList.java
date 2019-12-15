package com.example.domain.Common.entitylist;

import com.example.domain.Common.entity.Entity;
import com.example.domain.Common.entity.ID;
import com.example.domain.Common.function.Function;

import java.util.List;

public interface EntityList<E extends Entity> {

    // --------------------------------------Static-------------------------------------------------
    static <Type extends Entity> EntityList<Type> newList(List<Type> entityList) {
        return new EntityListImpl<>(entityList);
    }

    static <Type extends Entity> EntityList<Type> unmodifiableList(EntityList<Type> entityList) {
        return new UnmodifiableEntityList<>(entityList);
    }

    enum Mark {
        Modified,
        Removed
    }

    // ---------------------------------------Command-----------------------------------------------
    void put(E entity);

    void remove(ID id);

    void removeAll();

    // -----------------------------------------Query-----------------------------------------------
    E get(ID id);

    boolean contain(Function<E, Boolean> function);

    boolean contain(ID id);

    List<E> getAll();

    List<ID> getAll(Mark mark);

    int size();
}
