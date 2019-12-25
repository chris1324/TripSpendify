package com.example.domain.Cash.cashAccount;

import com.example.domain.Cash.cashRecord.CashRecord;
import com.example.domain.Shared.queryBaseClass.account.Account;
import com.example.domain.Shared.queryBaseClass.acountRecord.AccountRecord;
import com.example.domain.Shared.valueObject.date.Date;
import com.example.domain.Shared.valueObject.id.ID;
import com.example.domain.Shared.valueObject.note.Note;
import com.example.domain.Shared.valueObject.numeric.Amount;

import java.util.List;

public class CashAccount extends Account {

    // region Record -------------------------------------------------------------------------------
    public class Record extends AccountRecord<CashRecord.SourceType> {
        public Record(ID sourceTransId, CashRecord.SourceType source, Amount amount, Date date, Note note) {
            super(sourceTransId, source, amount, date, note);
        }
    }
    // endregion Record  ---------------------------------------------------------------------------

    // region Variables and Constructor ------------------------------------------------------------
    public final Amount mTotalBalance;
    public final List<Record> mRecords;
    
    public CashAccount(ID tripId, List<Record> cashAccRecords) {
        super(tripId);
        mRecords = cashAccRecords;
        mTotalBalance = this.calculateBalance(cashAccRecords);
    }
    // endregion Variables and Constructor ---------------------------------------------------------
}
