package com.example.data.Shared.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.data.Shared.schema.Schema;

import java.util.ArrayList;
import java.util.List;

@Dao
public abstract class BaseDao<S extends Schema> {

    // ----------------------------------------- Insert --------------------------------------------
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract protected long insert(S schema);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract protected List<Long> insert(List<S> schemas);

    // ----------------------------------------- Update --------------------------------------------
    @Update
    abstract protected int update(S schema);

    @Update
    abstract protected int update(List<S> schemas);

    // ----------------------------------------- Delete --------------------------------------------
    @Delete
    abstract public int delete(S schema);

    // ----------------------------------------- Upsert --------------------------------------------

    @Transaction
    public void upsert(S schemas) {
        long result = this.insert(schemas);
        if (result == -1) update(schemas);
    }

    @Transaction
    public void upsert(List<S> schemas) {
        List<Long> results = this.insert(schemas);
        List<S> toUpdateSchema = new ArrayList<>();

        for (int i = 0; i < results.size(); i++)
            if (results.get(i) == -1L) toUpdateSchema.add(schemas.get(i));

        if (!(toUpdateSchema.isEmpty())) update(toUpdateSchema);
    }



    // ---------------------------------------------------------------------------------------------
}
