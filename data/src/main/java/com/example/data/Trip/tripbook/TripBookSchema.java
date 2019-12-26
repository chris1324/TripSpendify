package com.example.data.Trip.tripbook;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.data.Shared.schema.Schema;

@Entity(tableName = "trip_book_table")
public class TripBookSchema extends Schema {

    @PrimaryKey()
    @ColumnInfo(name = "trip_book_Id")
    public final String tripBookId;

    public final String tripName;
    public final String photoUri;
    public final long startDate;
    public final long endDate;

    public TripBookSchema(String tripBookId, String tripName, String photoUri, long startDate, long endDate) {
        this.tripBookId = tripBookId;
        this.tripName = tripName;
        this.photoUri = photoUri;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
