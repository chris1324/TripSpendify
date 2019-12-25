package com.example.data.Trip.schema.category;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.example.data.Shared.schema.Schema;
import com.example.data.Trip.schema.tripbook.TripBookSchema;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "category_table",
        foreignKeys = @ForeignKey(
                entity = TripBookSchema.class,
                parentColumns = "trip_book_Id",
                childColumns = "tripBookId",
                onDelete = CASCADE)
)
public class CategorySchema extends Schema {

    @PrimaryKey()
    @ColumnInfo(name = "category_id")
    public final String categoryId;

    public final String tripBookId;
    public final String name;
    public final String iconUri;

    public CategorySchema(String categoryId, String tripBookId, String name, String iconUri) {
        this.categoryId = categoryId;
        this.tripBookId = tripBookId;
        this.name = name;
        this.iconUri = iconUri;
    }
}
