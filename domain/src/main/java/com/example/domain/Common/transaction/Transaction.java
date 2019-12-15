package com.example.domain.Common.transaction;

import com.example.domain.Common.entity.Entity;
import com.example.domain.Common.entity.ID;
import com.example.domain.Common.sharedvalueobject.date.Date;
import com.example.domain.Common.sharedvalueobject.monetaryamount.MonetaryAmount;
import com.example.domain.Common.sharedvalueobject.note.Note;

public class Transaction extends Entity {

    private final Date mDate;
    private final Note mNote;
    private final MonetaryAmount mAmount;

    public Transaction(ID id, Date date, Note note, MonetaryAmount amount) {
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

    public MonetaryAmount getAmount() {
        return mAmount;
    }
}
