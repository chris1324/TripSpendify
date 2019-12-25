package com.example.data.Trip.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.data.Shared.dao.BaseDao;
import com.example.data.Shared.dao.DaoHelper;
import com.example.data.Trip.schema.tripMember.TripMemberSchema;

import java.util.List;

@Dao
public abstract class TripMemberDAO  extends BaseDao<TripMemberSchema>  implements DaoHelper {

    @Query("SELECT * FROM trip_member_table WHERE trip_member_id=:tripMemberId")
    public abstract TripMemberSchema getById(String tripMemberId);

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
