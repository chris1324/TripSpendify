package com.example.domain.Shared.valueObject.note;

import com.example.domain.Shared.errorhanding.check.Check;
import com.example.domain.Shared.errorhanding.result.Result;

public class Note {


    // region Factory method -----------------------------------------------------------------------
    public static Result<Note,Err.Create> create(String note){
        if (Check.isNull(note)) return Result.err(Err.Create.NULL_NOTE);

        return Result.ok(new Note(note));
    }

    // endregion Factory method---------------------------------------------------------------------

    // region Error Class --------------------------------------------------------------------------
    public static class Err{
        public enum Create {
            NULL_NOTE
        }
    }
    // endregion Error Class -----------------------------------------------------------------------

    // region Variables and Constructor ------------------------------------------------------------
    private final String note;

    private Note(String note) {
        this.note = note;
    }

    // endregion Variables and Constructor ---------------------------------------------------------


    @Override
    public String toString() {
        return note;
    }
}
