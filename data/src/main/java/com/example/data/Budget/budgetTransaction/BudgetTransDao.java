package com.example.data.Budget.budgetTransaction;

import androidx.room.Query;

import com.example.data.Shared.dao.BaseDao;
import com.example.data.Shared.dao.DaoUtility;

import java.util.List;

public abstract class BudgetTransDao extends BaseDao<BudgetTransSchema> implements DaoUtility {

    @Query("SELECT * FROM budget_trans_table WHERE transactionId =:transactionId")
    public abstract BudgetTransSchema getById(String transactionId);

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
