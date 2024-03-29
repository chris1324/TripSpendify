package com.example.domain.Shared.commandBaseClass.transaction;

import com.example.domain.Shared.commandBaseClass.entity.Entity;
import com.example.domain.Shared.valueObject.date.Date;
import com.example.domain.Shared.valueObject.id.ID;
import com.example.domain.Shared.valueObject.note.Note;
import com.example.domain.Shared.valueObject.numeric.MonetaryAmount;

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
