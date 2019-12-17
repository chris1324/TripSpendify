package com.example.domain.Common.record;

import com.example.domain.Common.entity.Entity;
import com.example.domain.Common.entity.ID;

public class Record<Source> extends Entity {

    private final Source mSource;
    private final ID sourceTransId;

    public Record(ID id, Source source, ID sourceTransId) {
        super(id);
        mSource = source;
        this.sourceTransId = sourceTransId;
    }

    public Source getSource() {
        return mSource;
    }

    public ID getSourceTransId() {
        return sourceTransId;
    }
}
