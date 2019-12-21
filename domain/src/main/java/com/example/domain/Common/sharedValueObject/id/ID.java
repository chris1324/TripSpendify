package com.example.domain.Common.sharedValueObject.id;

import java.util.Objects;
import java.util.UUID;

public class ID {

    private final String uuid;

    public static ID existingId(String uuid){
        if (uuid.isEmpty()) throw new RuntimeException("uuid cant be empty");
        return new ID(uuid);
    }

    public static ID newId(){
        return new ID(UUID.randomUUID().toString());
    }


    private ID(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ID id = (ID) o;
        return uuid.equals(id.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}
