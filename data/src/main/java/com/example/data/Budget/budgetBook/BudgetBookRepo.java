package com.example.data.Budget.budgetBook;

import com.example.application.Budget.budgetBook.BudgetBookRepository;
import com.example.data.Budget.budgetRecord.BudgetRecordDao;
import com.example.data.Budget.budgetRecord.BudgetRecordRepo;
import com.example.data.Budget.budgetRecord.BudgetRecordSchemaMapper;
import com.example.data.Budget.budgetTransaction.BudgetTransDao;
import com.example.data.Budget.budgetTransaction.BudgetTransRepo;
import com.example.data.Budget.budgetTransaction.BudgetTransSchemaMapper;
import com.example.data.Shared.repository.AggregateRootRepository;
import com.example.data.Shared.repository.BaseRepository;
import com.example.data.Shared.transaction.TransactionDao;
import com.example.data.Shared.valueObjectMapper.ValueObjectMapper;
import com.example.domain.Budget.budgetBook.BudgetBook;
import com.example.domain.Shared.domainEventBus.DomainEventBus;
import com.example.domain.Shared.valueObject.id.ID;

public class BudgetBookRepo extends AggregateRootRepository<BudgetBook> implements BudgetBookRepository {

    // region Variables and Constructor ------------------------------------------------------------

    private final BudgetRecordRepo mBudgetRecordDao;
    private final BudgetTransRepo mBudgetTransDao;

    public BudgetBookRepo(ValueObjectMapper voMapper, BudgetRecordRepo budgetRecordDao, BudgetTransRepo budgetTransDao) {
        super(voMapper);
        mBudgetRecordDao = budgetRecordDao;
        mBudgetTransDao = budgetTransDao;
    }

    // endregion Variables and Constructor ---------------------------------------------------------

    // ---------------------------------------- Command --------------------------------------------
    @Override
    public void save(BudgetBook budgetBook) {
        mBudgetRecordDao.save(budgetBook.getBudgetRecords());
        mBudgetTransDao.save(budgetBook.getId(), budgetBook.getBudgetTransactions());
        super.save(budgetBook);
    }

    // ---------------------------------------- Query ----------------------------------------------

    @Override
    public BudgetBook getByTripId(ID tripBookId) {

        return null;
    }

    @Override
    public boolean exist(ID id) {
        return false;
    }

    // ---------------------------------------------------------------------------------------------
}
