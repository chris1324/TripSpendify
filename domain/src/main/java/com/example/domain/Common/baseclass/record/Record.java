package com.example.domain.Common.baseclass.record;

import com.example.domain.Common.baseclass.entity.Entity;
import com.example.domain.Common.sharedValueObject.id.ID;


public abstract  class Record<Source, Amount> extends Entity {

    public enum Effect{
        INCREASE,
        DECREASE
    }

    private final Source mSource;
    private final Amount mAmount;

    public Record(ID sourceTransId, Source source, Amount amount) {
        super(sourceTransId);
        mSource = source;
        mAmount = amount;
    }

    public Source getSource() {
        return mSource;
    }

    public ID getSourceTransId() {
        return getId();
    }

    public Amount getAmount() {
        return mAmount;
    }
}
