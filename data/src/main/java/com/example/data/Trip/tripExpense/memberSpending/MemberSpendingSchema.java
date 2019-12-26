package com.example.data.Trip.tripExpense.memberSpending;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import com.example.data.Shared.schema.Schema;
import com.example.data.Shared.transaction.TransactionSchema;
import com.example.data.Trip.tripMember.TripMemberSchema;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "member_spending_table",
        primaryKeys = {"transactionId", "memberId"},
        foreignKeys = {
                @ForeignKey(
                        entity = TransactionSchema.class,
                        parentColumns = "transaction_id",
                        childColumns = "transactionId",
                        onDelete = CASCADE
                ),
                @ForeignKey(
                        entity = TripMemberSchema.class,
                        parentColumns = "trip_member_id",
                        childColumns = "memberId",
                        onDelete = CASCADE
                )
        })
public class MemberSpendingSchema extends Schema {

    @ColumnInfo(index = true)
    public final String transactionId;
    @ColumnInfo(index = true)
    public final String memberId;

    public final double amount;

    public MemberSpendingSchema(String transactionId, String memberId, double amount) {
        this.transactionId = transactionId;
        this.memberId = memberId;
        this.amount = amount;
    }
}
