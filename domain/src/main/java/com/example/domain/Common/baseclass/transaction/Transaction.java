package com.example.domain.Common.baseclass.transaction;

import com.example.domain.Common.baseclass.entity.Entity;
import com.example.domain.Common.sharedValueObject.numeric.MonetaryAmount;
import com.example.domain.Common.sharedValueObject.id.ID;
import com.example.domain.Common.sharedValueObject.date.Date;
import com.example.domain.Common.sharedValueObject.note.Note;

public abstract class Transaction extends Entity {

    private final Date mDate;
    private final Note mNote;
    private final MonetaryAmount mAmount;

    protected Transaction(ID id, Date date, Note note, MonetaryAmount amount) {
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
