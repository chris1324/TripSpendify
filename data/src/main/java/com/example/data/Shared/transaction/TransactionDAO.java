package com.example.data.Shared.transaction;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.data.Shared.dao.BaseDao;
import com.example.data.Shared.dao.DaoHelper;

import java.lang.annotation.Documented;
import java.util.List;

@Dao
public abstract class TransactionDAO extends BaseDao<TransactionSchema> implements DaoHelper {

    @Query("SELECT * FROM transaction_table WHERE transaction_id=:transactionId")
    public abstract TransactionSchema getById(String transactionId);


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
