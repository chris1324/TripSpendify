package com.example.data.Trip.Category;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.data.Shared.dao.BaseDao;
import com.example.data.Shared.dao.DaoUtility;

import java.util.List;

import io.reactivex.Observable;

@Dao
public abstract class CategoryDao extends BaseDao<CategorySchema>implements DaoUtility {

    @Query("SELECT * FROM category_table WHERE category_id=:categoryId")
    public abstract CategorySchema getById(String categoryId);

    @Query("SELECT * FROM category_table WHERE tripBookId=:tripId")
    public abstract Observable<List<CategorySchema>> fetchByTripId(String tripId);

    @Query("SELECT * FROM category_table WHERE tripBookId=:tripId")
    public abstract List<CategorySchema> getByTripId(String tripId);

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
