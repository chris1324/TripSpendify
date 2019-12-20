package com.example.domain.Trip.tripexpense;

import com.example.domain.Common.errorhanding.exception.ImpossibleState;
import com.example.domain.Common.errorhanding.result.Result;
import com.example.domain.Common.sharedvalueobject.id.ID;
import com.example.domain.Common.sharedvalueobject.numeric.Amount;
import com.example.domain.Common.sharedvalueobject.numeric.MonetaryAmount;
import com.example.domain.Trip.tripexpense.splittingdetail.SplittingDetail;

import java.util.HashMap;
import java.util.Map;

public class SplittingDetailFactory {

    // region Error Class --------------------------------------------------------------------------
    public enum Err {
        NO_ONE_SPENDING
    }
    // endregion Error Class -----------------------------------------------------------------------

    public Result<SplittingDetail, Err> create(Amount userSpending, Map<ID, Amount> memberSpending) {
        // Prepare
        this.removeNegativeAndZeroAmount(memberSpending);
        boolean isUserSpending = userSpending.isPositive();
        boolean isMemberSpending = memberSpending.size() > 0;
        boolean isBothSpending = isUserSpending && isMemberSpending;

        // Check and Create
        if (isBothSpending) return this.createUserAndMemberSpend(userSpending, memberSpending);
        if (isMemberSpending) return this.createMemberSpend(memberSpending);
        if (isUserSpending) return this.createUserSpend(userSpending);


        // Handle Error
        return Result.err(Err.NO_ONE_SPENDING);
    }

    // region helper method ------------------------------------------------------------------------

    private Result<SplittingDetail, Err> createUserAndMemberSpend(Amount userSpending, Map<ID, Amount> memberSpending) {
        Map<ID, MonetaryAmount> memberSpendingMonetary = this.createMonetaryAmount(memberSpending);
        MonetaryAmount userSpendingMonetary = MonetaryAmount
                .create(userSpending)
                .getValue()
                .orElseThrow(ImpossibleState::new);

        return Result.ok(SplittingDetail
                .userAndMemberSpend(userSpendingMonetary, memberSpendingMonetary)
                .getValue()
                .orElseThrow(ImpossibleState::new));
    }

    private Result<SplittingDetail, Err> createMemberSpend(Map<ID, Amount> memberSpending) {
        Map<ID, MonetaryAmount> memberSpendingMonetary = this.createMonetaryAmount(memberSpending);

        return Result.ok(SplittingDetail
                .memberSpend(memberSpendingMonetary)
                .getValue()
                .orElseThrow(ImpossibleState::new));

    }

    private Result<SplittingDetail, Err> createUserSpend(Amount userSpending) {
        MonetaryAmount userSpendingMonetary = MonetaryAmount
                .create(userSpending)
                .getValue()
                .orElseThrow(ImpossibleState::new);

        return Result.ok(SplittingDetail
                .userSpend(userSpendingMonetary)
                .getValue()
                .orElseThrow(ImpossibleState::new));
    }

    // ---------------------------------------------------------------------------------------------
    private void removeNegativeAndZeroAmount(Map<ID, Amount> memberPayment) {
        for (ID id : memberPayment.keySet())
            if (!(memberPayment.get(id).isPositive())) memberPayment.remove(id);
    }

    private Map<ID, MonetaryAmount> createMonetaryAmount(Map<ID, Amount> memberPayment) {
        Map<ID, MonetaryAmount> memberPaymentMonetary = new HashMap<>();
        memberPayment.forEach((id, amount) -> memberPaymentMonetary.put(id, MonetaryAmount
                .create(amount)
                .getValue()
                .orElseThrow(ImpossibleState::new)));
        return memberPaymentMonetary;
    }

    // endregion helper method ---------------------------------------------------------------------
}
