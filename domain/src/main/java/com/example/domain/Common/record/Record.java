package com.example.domain.Common.record;

import com.example.domain.Common.entity.Entity;
import com.example.domain.Common.entity.ID;

public class Record<Source> extends Entity {

    private final Source mSource;

    public Record(ID id, Source source) {
        super(id);
        mSource = source;
    }

    public Source getSource() {
        return mSource;
    }
}
