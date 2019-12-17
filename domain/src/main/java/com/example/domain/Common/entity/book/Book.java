package com.example.domain.Common.entity.book;

import com.example.domain.Common.entity.Entity;
import com.example.domain.Common.entity.ID;

public abstract class Book extends Entity {
    private final ID mTripBookID;

    public Book(ID id, ID tripBookID) {
        super(id);
        mTripBookID = tripBookID;
    }

    public ID getTripBookID() {
        return mTripBookID;
    }

    public boolean isSameTrip(Book book) {
        return mTripBookID.equals(book.getTripBookID());
    }

    public boolean isSameTrip(Book... books) {
        for (Book b : books) {
            if (this.isSameTrip(b)) return true;
        }
        return false;
    }

}
