package com.example.data.Shared.transaction;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.data.Shared.dao.BaseDao;
import com.example.data.Shared.dao.DaoUtility;

import java.util.List;

import io.reactivex.Observable;

@Dao
public abstract class TransactionDao extends BaseDao<TransactionSchema> implements DaoUtility {

    @Query("SELECT * FROM transaction_table WHERE transaction_id=:transactionId")
    public abstract TransactionSchema getById(String transactionId);

    @Query("SELECT * FROM transaction_table WHERE tripBookId=:tripId")
    public abstract List<TransactionSchema> getByTripId(String tripId);


    @Query("SELECT * FROM transaction_table WHERE tripBookId=:tripId")
    public abstract Observable<List<TransactionSchema>> fetchByTripId(String tripId);

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
