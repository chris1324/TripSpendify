package com.example.data.Shared.transaction;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.example.data.Shared.schema.Schema;
import com.example.data.Trip.tripbook.TripBookSchema;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "transaction_table",
        foreignKeys = @ForeignKey(
                entity = TripBookSchema.class,
                parentColumns = "trip_book_Id",
                childColumns = "mTripBookId",
                onDelete = CASCADE)
)
public class TransactionSchema extends Schema {

    @PrimaryKey()
    @ColumnInfo(name = "transaction_id")
    public final String transactionId;

    public final String tripBookId;
    public final long date;
    public final double amount;
    public final String note;

    public TransactionSchema(String transactionId, String tripBookId, long date, double amount, String note) {
        this.transactionId = transactionId;
        this.tripBookId = tripBookId;
        this.date = date;
        this.amount = amount;
        this.note = note;
    }
}
