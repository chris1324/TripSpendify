package com.example.data.Cash.cashRecord;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import com.example.data.Shared.schema.Schema;
import com.example.data.Shared.transaction.TransactionSchema;

import static androidx.room.ForeignKey.CASCADE;


@Entity(tableName = "cash_record_table",
        primaryKeys = "sourceTransId",
        foreignKeys = @ForeignKey(
                entity = TransactionSchema.class,
                parentColumns = "transaction_id",
                childColumns = "sourceTransId",
                onDelete = CASCADE)
)

public class CashRecordSchema extends Schema {

    @ColumnInfo(index = true)
    public final String sourceTransId;
    public final String sourceTransType;
    public final double recordAmount;

    public CashRecordSchema(String sourceTransId, String sourceTransType, double recordAmount) {
        this.sourceTransId = sourceTransId;
        this.sourceTransType = sourceTransType;
        this.recordAmount = recordAmount;
    }
}
