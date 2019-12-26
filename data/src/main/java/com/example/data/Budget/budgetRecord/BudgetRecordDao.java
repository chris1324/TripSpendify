package com.example.data.Budget.budgetRecord;

import androidx.room.Query;

import com.example.data.Budget.budgetTransaction.BudgetTransSchema;
import com.example.data.Shared.dao.BaseDao;
import com.example.data.Shared.dao.DaoUtility;

import java.util.List;

public abstract class BudgetRecordDao extends BaseDao<BudgetRecordSchema> implements DaoUtility {

    @Query("SELECT * FROM budget_record_table WHERE sourceTransId =:transactionId")
    public abstract BudgetRecordSchema getById(String transactionId);


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
