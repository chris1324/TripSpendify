package com.example.domain.Common.entity.transaction;

import com.example.domain.Common.entity.Entity;
import com.example.domain.Common.entity.ID;
import com.example.domain.Common.sharedvalueobject.amount.Amount;
import com.example.domain.Common.sharedvalueobject.date.Date;
import com.example.domain.Common.sharedvalueobject.amount.MonetaryAmount;
import com.example.domain.Common.sharedvalueobject.note.Note;

public abstract class Transaction<A extends Amount> extends Entity {

    private final Date mDate;
    private final Note mNote;
    private final A mAmount;

    protected Transaction(ID id, Date date, Note note, A amount) {
        super(id);
        mDate = date;
        mNote = note;
        mAmount = amount;
    }

    public Date getDate() {
        return mDate;
    }

    public Note getNote() {
        return mNote;
    }

    public A getAmount() {
        return mAmount;
    }
}
