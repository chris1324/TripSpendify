package com.example.data.Budget.budgetRecord;

import com.example.data.Shared.repository.BaseRepository;
import com.example.data.Shared.valueObjectMapper.ValueObjectMapper;
import com.example.domain.Budget.budgetRecord.BudgetRecord;
import com.example.domain.Shared.entityList.EntityList;
import com.example.domain.Shared.valueObject.id.ID;

import java.util.stream.Collectors;

public class BudgetRecordRepo extends BaseRepository {

    // region Variables and Constructor ------------------------------------------------------------
    private final BudgetRecordDao mBudgetRecordDao;
    private final BudgetRecordSchemaMapper mSchemaMapper;

    public BudgetRecordRepo(ValueObjectMapper voMapper, BudgetRecordDao budgetRecordDao, BudgetRecordSchemaMapper schemaMapper) {
        super(voMapper);
        mBudgetRecordDao = budgetRecordDao;
        mSchemaMapper = schemaMapper;
    }
    // endregion Variables and Constructor ---------------------------------------------------------

    // ---------------------------------------- Command --------------------------------------------

    public void save(EntityList<BudgetRecord> budgetRecords) {
        mBudgetRecordDao.delete(budgetRecords.
                getRemoved().stream()
                .map(this::mapID)
                .collect(Collectors.toList()));

        mBudgetRecordDao.upsert(budgetRecords
                .getModified().stream()
                .map(mSchemaMapper::mapToSchema)
                .collect(Collectors.toList()));
    }

    // ---------------------------------------------------------------------------------------------

    public EntityList<BudgetRecord> getByTripId(ID tripBookId){

    }
}
