package com.example.data.Budget.budgetTransaction;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import com.example.data.Shared.schema.Schema;
import com.example.data.Shared.transaction.TransactionSchema;
import com.example.data.Trip.Category.CategorySchema;

import static androidx.room.ForeignKey.CASCADE;


@Entity(tableName = "budget_trans_table",
        primaryKeys = "transactionId",
        foreignKeys = {
                @ForeignKey(
                        entity = TransactionSchema.class,
                        parentColumns = "transaction_id",
                        childColumns = "transactionId",
                        onDelete = CASCADE),
                @ForeignKey(
                        entity = CategorySchema.class,
                        parentColumns = "category_id",
                        childColumns = "categoryId",
                        onDelete = CASCADE)}
)
public class BudgetTransSchema extends Schema {


    @ColumnInfo(index = true)
    public final String transactionId;

    public final String categoryId;

    public BudgetTransSchema(String transactionId, String categoryId) {
        this.transactionId = transactionId;
        this.categoryId = categoryId;
    }
}
