package com.example.domain.Common.sharedvalueobject.contactnumber;

import com.example.domain.Common.errorhanding.check.Check;
import com.example.domain.Common.errorhanding.result.Result;

public class ContactNumber {

    // region Factory method -----------------------------------------------------------------------
    public static  Result<ContactNumber,Err.Create> create(String contactNumber){
        if (Check.isNull(contactNumber)) return Result.err(Err.Create.CONTRACT_NUMBER_NULL);
        if (Check.isEmptyList(contactNumber)) return Result.err(Err.Create.CONTRACT_NUMBER_EMPTY);

        return Result.ok(new ContactNumber(contactNumber));
    }

    // endregion Factory method---------------------------------------------------------------------

    // region Error Class --------------------------------------------------------------------------
    public static class Err{
        public enum Create{
            CONTRACT_NUMBER_NULL,
            CONTRACT_NUMBER_EMPTY,
            CONTRACT_NUMBER_INVALID
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
