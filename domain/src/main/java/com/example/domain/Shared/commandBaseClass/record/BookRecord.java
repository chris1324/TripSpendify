package com.example.domain.Shared.commandBaseClass.record;

import com.example.domain.Shared.commandBaseClass.entity.Entity;
import com.example.domain.Shared.valueObject.id.ID;


public abstract class BookRecord<S extends BookRecord.Source, Amount> extends Entity {

    public interface Source {
        public enum Effect{
            INCREASE,
            DECREASE
        }

        Effect effectOnBalance();
    }

    private final S mSource;
    private final Amount mAmount;

    public BookRecord(ID sourceTransId, S source, Amount amount) {
        super(sourceTransId);
        mSource = source;
        mAmount = amount;
    }

    public S getSource() {
        return mSource;
    }

    public ID getSourceTransId() {
        return getId();
    }

    public Amount getAmount() {
        return mAmount;
    }
}
