package com.example.domain.Shared.commandBaseClass.book;

import com.example.domain.Shared.commandBaseClass.aggregateroot.AggregateRoot;
import com.example.domain.Shared.valueObject.id.ID;

public abstract class Book extends AggregateRoot {

    public Book(ID tripBookID) {
        super(tripBookID);
    }

    public ID getTripID() {
        return getId();
    }

    public boolean isSameTrip(Book book) {
        return getTripID().equals(book.getTripID());
    }

    public boolean isSameTrip(Book... books) {
        for (Book b : books) {
            if (this.isSameTrip(b)) return true;
        }
        return false;
    }

}
