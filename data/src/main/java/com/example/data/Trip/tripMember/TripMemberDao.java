package com.example.data.Trip.tripMember;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.data.Shared.dao.BaseDao;
import com.example.data.Shared.dao.DaoUtility;

import java.util.List;

import io.reactivex.Observable;

@Dao
public abstract class TripMemberDao extends BaseDao<TripMemberSchema> implements DaoUtility {

    @Query("SELECT * FROM trip_member_table WHERE trip_member_id=:tripMemberId")
    public abstract TripMemberSchema getById(String tripMemberId);

    @Query("SELECT * FROM trip_member_table WHERE tripBookId=:tripId")
    public abstract Observable<List<TripMemberSchema>> fetchByTripId(String tripId);

    @Query("SELECT * FROM trip_member_table WHERE tripBookId=:tripId")
    public abstract List<TripMemberSchema> getByTripId(String tripId);

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
