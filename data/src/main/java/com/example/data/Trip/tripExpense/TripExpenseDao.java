package com.example.data.Trip.tripExpense;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.data.Shared.dao.BaseDao;
import com.example.data.Shared.dao.DaoUtility;

import java.util.List;

import io.reactivex.Observable;

@Dao
public abstract class TripExpenseDao extends BaseDao<TripExpenseSchema> implements DaoUtility {

    @Query("SELECT * FROM trip_expense_table WHERE transactionId =:transactionId")
    public abstract TripExpenseSchema getById(String transactionId);

    @Query("SELECT * FROM trip_expense_table WHERE transactionId =:transactionId")
    public abstract Observable<TripExpenseSchema> fetchById(String transactionId);

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
