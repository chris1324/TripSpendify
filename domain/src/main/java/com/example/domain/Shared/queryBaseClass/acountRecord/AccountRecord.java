package com.example.domain.Shared.queryBaseClass.acountRecord;

import com.example.domain.Shared.commandBaseClass.record.BookRecord;
import com.example.domain.Shared.valueObject.date.Date;
import com.example.domain.Shared.valueObject.id.ID;
import com.example.domain.Shared.valueObject.note.Note;
import com.example.domain.Shared.valueObject.numeric.Amount;

public  abstract class AccountRecord<SourceType extends BookRecord.Source> {

   public final ID mSourceTransId;
   public final SourceType mSource;
   public final Amount mAmount;
   public final Date mDate;
   public final Note mNote;

    public AccountRecord(ID sourceTransId, SourceType source, Amount amount, Date date, Note note) {
        mSourceTransId = sourceTransId;
        mSource = source;
        mAmount = amount;
        mDate = date;
        mNote = note;
    }
}
