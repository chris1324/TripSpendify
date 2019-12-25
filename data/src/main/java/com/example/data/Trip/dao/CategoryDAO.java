package com.example.data.Trip.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.data.Shared.dao.BaseDao;
import com.example.data.Shared.dao.DaoHelper;
import com.example.data.Trip.schema.category.CategorySchema;
import com.example.data.Trip.schema.tripMember.TripMemberSchema;

import java.util.List;

@Dao
public abstract class CategoryDAO extends BaseDao<CategorySchema> implements DaoHelper {

    @Query("SELECT * FROM category_table WHERE category_id=:categoryId")
    public abstract CategorySchema getById(String categoryId);

    // region helper method ------------------------------------------------------------------------
    @Override
    public void delete(String id) {
        this.delete(this.getById(id));
    }

    @Override
    public void delete(List<String> ids) {
        ids.forEach(this::delete);
    }

    @Override
    public boolean exist(String id) {
        return this.getById(id) != null;
    }


    // endregion helper method ---------------------------------------------------------------------

}


