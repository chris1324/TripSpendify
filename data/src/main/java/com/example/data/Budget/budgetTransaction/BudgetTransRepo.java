package com.example.data.Budget.budgetTransaction;

import com.example.data.Shared.repository.BaseRepository;
import com.example.data.Shared.transaction.TransactionDao;
import com.example.data.Shared.valueObjectMapper.ValueObjectMapper;
import com.example.domain.Budget.budgetTransaction.BudgetTransaction;
import com.example.domain.Shared.entityList.EntityList;
import com.example.domain.Shared.valueObject.id.ID;

import java.util.stream.Collectors;

public class BudgetTransRepo extends BaseRepository {

    // region Variables and Constructor ------------------------------------------------------------
    private final TransactionDao mTransactionDao;
    private final BudgetTransDao mBudgetTransDao;
    private final BudgetTransSchemaMapper mSchemaMapper;

    public BudgetTransRepo(ValueObjectMapper voMapper, TransactionDao transactionDao, BudgetTransDao budgetTransDao, BudgetTransSchemaMapper schemaMapper) {
        super(voMapper);
        mTransactionDao = transactionDao;
        mBudgetTransDao = budgetTransDao;
        mSchemaMapper = schemaMapper;
    }

    // endregion Variables and Constructor ---------------------------------------------------------

    // ---------------------------------------- Command --------------------------------------------
    public void save(ID tripBookId, EntityList<BudgetTransaction> budgetTransactions) {
        mBudgetTransDao.delete(budgetTransactions
                .getRemoved().stream()
                .map(this::mapID)
                .collect(Collectors.toList()));

        budgetTransactions
                .getModified().stream()
                .map(trans -> new BudgetTransSchemaMapper.Domain(tripBookId, trans))
                .map(mSchemaMapper::mapToSchema)
                .forEach(schema -> {
                            mTransactionDao.upsert(schema.mTransactionSchema);
                            mBudgetTransDao.upsert(schema.mBudgetTransSchema);
                        }
                );
    }

    // ---------------------------------------------------------------------------------------------
}
