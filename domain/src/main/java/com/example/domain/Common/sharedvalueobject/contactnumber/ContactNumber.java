package com.example.domain.Common.sharedvalueobject.contactnumber;

import com.example.domain.Common.errorhanding.check.Check;
import com.example.domain.Common.errorhanding.result.Result;

public class ContactNumber {

    // region Factory method -----------------------------------------------------------------------
    public Result<ContactNumber,Err.Create> create(String contactNumber){
        if (Check.isNull(contactNumber)) return Result.err(Err.Create.NULL_CONTRACT_NUMBER);
        if (Check.isEmptyString(contactNumber)) return Result.err(Err.Create.EMPTY_CONTRACT_NUMBER);

        return Result.ok(new ContactNumber(contactNumber));
    }

    // endregion Factory method---------------------------------------------------------------------

    // region Error Class --------------------------------------------------------------------------
    public static class Err{
        public enum Create{
            NULL_CONTRACT_NUMBER,
            EMPTY_CONTRACT_NUMBER,
            INVALID_CONTRACT_NUMBER
        }
    }
    // endregion Error Class -----------------------------------------------------------------------

    // region Variables and Constructor ------------------------------------------------------------

    private final String contactNumber;
    private ContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    // endregion Variables and Constructor ---------------------------------------------------------

}
