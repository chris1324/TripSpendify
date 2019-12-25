package com.example.data.Collectible.schema;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.example.data.Shared.schema.Schema;
import com.example.data.Shared.transaction.TransactionSchema;
import com.example.data.Trip.schema.category.CategorySchema;
import com.example.data.Trip.schema.tripMember.TripMemberSchema;

import static androidx.room.ForeignKey.CASCADE;


@Entity(tableName = "coll_trans_table",
        primaryKeys = "transactionId",
        foreignKeys = {
                @ForeignKey(
                        entity = TransactionSchema.class,
                        parentColumns = "transaction_id",
                        childColumns = "transactionId",
                        onDelete = CASCADE),
                @ForeignKey(
                        entity = TripMemberSchema.class,
                        parentColumns = "trip_member_id",
                        childColumns = "involvedMemberId",
                        onDelete = CASCADE)}
)
public class CollTransSchema extends Schema {

    @ColumnInfo(index = true)
    public final String transactionId;

    public final String involvedMemberId;

    public final String payer;
    public final String transactionType;

    public CollTransSchema(String transactionId, String involvedMemberId, String payer, String transactionType) {
        this.transactionId = transactionId;
        this.involvedMemberId = involvedMemberId;
        this.payer = payer;
        this.transactionType = transactionType;
    }
}
