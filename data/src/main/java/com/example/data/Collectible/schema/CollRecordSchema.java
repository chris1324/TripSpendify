package com.example.data.Collectible.schema;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.example.data.Shared.schema.Schema;
import com.example.data.Shared.transaction.TransactionSchema;
import com.example.data.Trip.schema.tripMember.TripMemberSchema;

import static androidx.room.ForeignKey.CASCADE;


@Entity(tableName = "coll_record_table",
        primaryKeys = "sourceTransId,memberId",
        foreignKeys = {
                @ForeignKey(
                        entity = TransactionSchema.class,
                        parentColumns = "transaction_id",
                        childColumns = "sourceTransId",
                        onDelete = CASCADE),
                @ForeignKey(
                        entity = TripMemberSchema.class,
                        parentColumns = "trip_member_id",
                        childColumns = "memberId",
                        onDelete = CASCADE
                )
        }
)
public class CollRecordSchema extends Schema {

    @ColumnInfo(index = true)
    public final String sourceTransId;
    @ColumnInfo(index = true)
    public final String memberId;

    public final String sourceTransType;
    public final double recordAmount;

    public CollRecordSchema(String sourceTransId, String memberId, String sourceTransType, double recordAmount) {
        this.sourceTransId = sourceTransId;
        this.memberId = memberId;
        this.sourceTransType = sourceTransType;
        this.recordAmount = recordAmount;
    }
}
