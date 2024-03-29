package com.example.domain.Shared.valueObject.name;

import com.example.domain.Shared.errorhanding.check.Check;
import com.example.domain.Shared.errorhanding.result.Result;

public class Name {

    // region Factory method -----------------------------------------------------------------------
    private static final int STRING_MIN_CHAR = 4;
    public static Result<Name,Err.Create> create(String name) {
        if (Check.isNull(name)) return  Result.err(Err.Create.NAME_NULL);
        if (name.length() < STRING_MIN_CHAR) return Result.err(Err.Create.NAME_CHAR_LESS_THAN_FOUR);

        return Result.ok(new Name(name));
    }
    // endregion Factory method---------------------------------------------------------------------

    // region Error Class --------------------------------------------------------------------------
    public static class Err{
        public enum Create{
            NAME_NULL,
            NAME_CHAR_LESS_THAN_FOUR
        }
    }
    // endregion Error Class -----------------------------------------------------------------------

    // region Variables and Constructor ------------------------------------------------------------
    private final String name;

    private Name(String name) {
        this.name = name;
    }
    // endregion Variables and Constructor ---------------------------------------------------------

    @Override
    public String toString() {
        return name;
    }
}
