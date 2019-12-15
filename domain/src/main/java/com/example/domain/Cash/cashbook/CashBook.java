package com.example.domain.Cash.cashbook;

import com.example.domain.Cash.cashrecord.CashRecord;
import com.example.domain.Common.entity.Entity;
import com.example.domain.Common.entity.ID;
import com.example.domain.Common.entitylist.EntityList;

public class CashBook extends Entity {

    // region Factory method -----------------------------------------------------------------------
    // endregion Factory method---------------------------------------------------------------------

    // region Error Class --------------------------------------------------------------------------
    public static class Err{
        public enum Create{

        }
    }
    // endregion Error Class -----------------------------------------------------------------------

    // region Variables and Constructor ------------------------------------------------------------
    private final ID mTripBookId;
    private final EntityList<CashRecord> mCashTransactions;
    private final EntityList<CashDeposit> mCashDeposits;
    private final EntityList<CashWithdrawal> mCashWithdrawals;
    private final EntityList<CashAdjustment> mCashAdjustments;

    public CashBook(ID id,
                    ID tripBookId,
                    EntityList<CashRecord> cashTransactions,
                    EntityList<CashDeposit> cashDeposits,
                    EntityList<CashWithdrawal> cashWithdrawals,
                    EntityList<CashAdjustment> cashAdjustments) {
        super(id);
        mTripBookId = tripBookId;
        mCashTransactions = cashTransactions;
        mCashDeposits = cashDeposits;
        mCashWithdrawals = cashWithdrawals;
        mCashAdjustments = cashAdjustments;
    }


    // endregion Variables and Constructor ---------------------------------------------------------

    // ---------------------------------------------------------------------------------------------

    // region helper method ------------------------------------------------------------------------
    // endregion helper method ---------------------------------------------------------------------

    // region Getter -------------------------------------------------------------------------------
    // endregion Getter ----------------------------------------------------------------------------
}
