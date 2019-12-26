package com.example.data.Budget.budgetTransaction;

import com.example.data.Shared.mapper.SchemaMapper;
import com.example.data.Shared.mapper.TransactionSchemaMapper;
import com.example.data.Shared.transaction.TransactionSchema;
import com.example.data.Shared.valueObjectMapper.ValueObjectMapper;
import com.example.domain.Budget.budgetBook.BudgetTransactionService;
import com.example.domain.Budget.budgetTransaction.BudgetTransaction;
import com.example.domain.Shared.errorhanding.exception.ImpossibleState;
import com.example.domain.Shared.valueObject.id.ID;

public class BudgetTransSchemaMapper extends TransactionSchemaMapper<BudgetTransSchemaMapper.Domain, BudgetTransSchemaMapper.Schema> {


    // region Variables and Constructor ------------------------------------------------------------
    public BudgetTransSchemaMapper(ValueObjectMapper voMapper) {
        super(voMapper);
    }
    // endregion Variables and Constructor ---------------------------------------------------------

    // region Domain -------------------------------------------------------------------------------
    public static class Domain {
        public final ID tripBookId;
        public final BudgetTransaction mBudgetTransaction;

        public Domain(ID tripBookId, BudgetTransaction budgetTransaction) {
            this.tripBookId = tripBookId;
            mBudgetTransaction = budgetTransaction;
        }
    }
    // endregion Domain ----------------------------------------------------------------------------

    // region Schema -------------------------------------------------------------------------------
    public static class Schema {
        public final TransactionSchema mTransactionSchema;
        public final BudgetTransSchema mBudgetTransSchema;

        public Schema(TransactionSchema transactionSchema, BudgetTransSchema budgetTransSchema) {
            mTransactionSchema = transactionSchema;
            mBudgetTransSchema = budgetTransSchema;
        }
    }
    // endregion Schema ----------------------------------------------------------------------------

    @Override
    public Schema mapToSchema(Domain domain) {
        return new Schema(
                this.mapTransaction(domain.tripBookId, domain.mBudgetTransaction),
                this.mapBudgetTransaction(domain.mBudgetTransaction)
        );
    }

    @Override
    public Domain mapToDomain(Schema schema) {
        return new Domain(
                this.mapID(schema.mTransactionSchema.tripBookId),
                this.mapBudgetTransaction(schema)
        );
    }

    // region helper method ------------------------------------------------------------------------
    private BudgetTransSchema mapBudgetTransaction(BudgetTransaction budgetTransaction) {
        return new BudgetTransSchema(
                this.mapID(budgetTransaction.getId()),
                this.mapID(budgetTransaction.getCategoryId()));
    }

    private BudgetTransaction mapBudgetTransaction(Schema schema) {
        return BudgetTransaction
                .create(
                        this.mapID(schema.mTransactionSchema.transactionId),
                        this.mapDate(schema.mTransactionSchema.date),
                        this.mapNote(schema.mTransactionSchema.note),
                        this.mapMonetaryAmount(schema.mTransactionSchema.amount),
                        this.mapID(schema.mBudgetTransSchema.categoryId))
                .getValue()
                .orElseThrow(ImpossibleState::new);
    }

    // endregion helper method ---------------------------------------------------------------------
}
