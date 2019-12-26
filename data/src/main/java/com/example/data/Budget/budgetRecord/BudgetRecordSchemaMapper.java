package com.example.data.Budget.budgetRecord;

import com.example.data.Shared.mapper.SchemaMapper;
import com.example.data.Shared.valueObjectMapper.ValueObjectMapper;
import com.example.domain.Budget.budgetRecord.BudgetRecord;
import com.example.domain.Shared.errorhanding.exception.ImpossibleState;

public class BudgetRecordSchemaMapper extends SchemaMapper<BudgetRecord, BudgetRecordSchema> {

    // region Variables and Constructor ------------------------------------------------------------

    public BudgetRecordSchemaMapper(ValueObjectMapper voMapper) {
        super(voMapper);
    }

    // endregion Variables and Constructor ---------------------------------------------------------

    @Override
    public BudgetRecordSchema mapToSchema(BudgetRecord budgetRecord) {
        return new BudgetRecordSchema(
                this.mapID(budgetRecord.getId()),
                this.mapEnum(budgetRecord.getSource()),
                this.mapMonetaryAmount(budgetRecord.getAmount()));
    }

    @Override
    public BudgetRecord mapToDomain(BudgetRecordSchema budgetRecordSchema) {
        return BudgetRecord
                .create(
                        this.mapID(budgetRecordSchema.sourceTransId),
                        BudgetRecord.SourceType.valueOf(budgetRecordSchema.sourceTransType),
                        this.mapMonetaryAmount(budgetRecordSchema.recordAmount))
                .getValue()
                .orElseThrow(ImpossibleState::new);
    }

    // ---------------------------------------------------------------------------------------------
}

