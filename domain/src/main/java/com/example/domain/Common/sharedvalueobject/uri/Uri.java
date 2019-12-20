package com.example.domain.Common.sharedvalueobject.uri;

import com.example.domain.Common.errorhanding.check.Check;
import com.example.domain.Common.errorhanding.result.Result;

public class Uri {

    // region Factory method -----------------------------------------------------------------------
    public static Result<Uri,Err.Create> create (String uri){
        if (Check.isNull(uri)) return Result.err(Err.Create.URI_NULL);

        return Result.ok(new Uri(uri));
    }
    // endregion Factory method---------------------------------------------------------------------

    // region Error Class --------------------------------------------------------------------------
    public static class Err{
        public enum Create {
            URI_NULL,
            URI_INVALID
        }
    }
    // endregion Error Class -----------------------------------------------------------------------

    // region Variables and Constructor ------------------------------------------------------------
    private final String uri;

    private Uri(String uri) {
        this.uri = uri;
    }
    // endregion Variables and Constructor ---------------------------------------------------------



    public String getUri() {
        return uri;
    }
}
