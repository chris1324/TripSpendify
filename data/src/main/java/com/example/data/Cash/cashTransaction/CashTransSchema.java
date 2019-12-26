package com.example.data.Cash.cashTransaction;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import com.example.data.Shared.schema.Schema;
import com.example.data.Shared.transaction.TransactionSchema;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "cash_trans_table",
        primaryKeys = "transactionId",
        foreignKeys = @ForeignKey(
                entity = TransactionSchema.class,
                parentColumns = "transaction_id",
                childColumns = "transactionId",
                onDelete = CASCADE)
)
public class CashTransSchema extends Schema {

    @ColumnInfo(index = true)
    public final String transactionId;
    public final String transactionType;

    public CashTransSchema(String transactionId, String transactionType) {
        this.transactionId = transactionId;
        this.transactionType = transactionType;
    }
}
