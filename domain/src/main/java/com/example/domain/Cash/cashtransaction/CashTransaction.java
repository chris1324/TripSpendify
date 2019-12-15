package com.example.domain.Cash.cashtransaction;

import com.example.domain.Common.entity.ID;
import com.example.domain.Common.sharedvalueobject.date.Date;
import com.example.domain.Common.sharedvalueobject.monetaryamount.MonetaryAmount;
import com.example.domain.Common.sharedvalueobject.note.Note;
import com.example.domain.Common.transaction.Transaction;

public class CashTransaction extends Transaction {

    public enum Type{
        WITHDRAWAL,
        DEPOSIT,
        ADJUSTMENT
    }

    private final Type mTransactionType;


    public CashTransaction(ID id, Date date, Note note, MonetaryAmount amount, Type transactionType) {
        super(id, date, note, amount);
        mTransactionType = transactionType;
    }
}
