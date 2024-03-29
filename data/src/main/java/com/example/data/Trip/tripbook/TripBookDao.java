package com.example.data.Trip.tripbook;

import androidx.room.Query;

import com.example.data.Shared.dao.BaseDao;
import com.example.data.Shared.dao.DaoUtility;

import java.util.List;

import io.reactivex.Observable;

public abstract class TripBookDao extends BaseDao<TripBookSchema>  implements DaoUtility {

    @Query("SELECT * FROM trip_book_table WHERE trip_book_Id=:tripId")
    public abstract TripBookSchema getById(String tripId);

    @Query("SELECT * FROM trip_book_table WHERE trip_book_Id=:tripId")
    public abstract Observable<TripBookSchema> fetchById(String tripId);

    @Query("SELECT * FROM trip_book_table")
    public abstract Observable<List<TripBookSchema>> fetchAll();

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
