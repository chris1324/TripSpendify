package com.example.data.Trip.schema.tripMember;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.example.data.Shared.schema.Schema;
import com.example.data.Trip.schema.tripbook.TripBookSchema;

@Entity(tableName = "trip_member_table",
        foreignKeys = @ForeignKey(
                entity = TripBookSchema.class,
                parentColumns = "trip_book_Id",
                childColumns = "tripBookId")
)
public class TripMemberSchema extends Schema {

    @PrimaryKey()
    @ColumnInfo(name = "trip_member_id")
    public final String memberId;

    public final String tripBookId;
    public final String name;
    public final String photoUri;
    public final String contactNumber;

    public TripMemberSchema(String memberId, String tripBookId, String name, String photoUri, String contactNumber) {
        this.memberId = memberId;
        this.tripBookId = tripBookId;
        this.name = name;
        this.photoUri = photoUri;
        this.contactNumber = contactNumber;
    }
}
