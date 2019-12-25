package com.example.data.Trip.schema.tripExpense;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.example.data.Shared.schema.Schema;
import com.example.data.Shared.transaction.TransactionSchema;
import com.example.data.Trip.schema.category.CategorySchema;
import com.example.data.Trip.schema.tripbook.TripBookSchema;

import static androidx.room.ForeignKey.CASCADE;


@Entity(tableName = "trip_expense_table",
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
public class TripExpenseSchema extends Schema {

    @ColumnInfo(index = true)
    public final String transactionId;
    public final String categoryId;

    public final boolean isUnpaid;
    public final double userPayment;
    public final double userSpending;

    public TripExpenseSchema(String transactionId, String categoryId, boolean isUnpaid, double userPayment, double userSpending) {
        this.transactionId = transactionId;
        this.categoryId = categoryId;
        this.isUnpaid = isUnpaid;
        this.userPayment = userPayment;
        this.userSpending = userSpending;
    }
}
