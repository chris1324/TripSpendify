package com.example.domain.Trip.tripexpense;

import com.example.domain.Common.errorhanding.exception.ImpossibleState;
import com.example.domain.Common.errorhanding.result.Result;
import com.example.domain.Common.sharedvalueobject.id.ID;
import com.example.domain.Common.sharedvalueobject.numeric.Amount;
import com.example.domain.Common.sharedvalueobject.numeric.MonetaryAmount;
import com.example.domain.Trip.tripexpense.paymentdetail.PaymentDetail;

import java.util.HashMap;
import java.util.Map;

public class PaymentDetailFactory {
    // region Error Class --------------------------------------------------------------------------
    public enum Err {
        NO_ONE_PAYING
    }
    // endregion Error Class -----------------------------------------------------------------------


    public Result<PaymentDetail, Err> create(Amount userPayment, Map<ID, Amount> memberPayment, boolean isUnpaid) {
        // UnPaid
        if (isUnpaid) return Result.ok(PaymentDetail.unpaid());

        // Paid
        // -- prepare
        this.removeNegativeAndZeroAmount(memberPayment);
        boolean isUserPaying = userPayment.isPositive();
        boolean isMemberPaying = memberPayment.size() > 0;
        boolean isBothPaying = isUserPaying && isMemberPaying;

        // -- Check and Create
        if (isBothPaying) return this.createUserAndMemberPay(userPayment, memberPayment);
        if (isMemberPaying) return this.createMemberPay(memberPayment);
        if (isUserPaying) return this.createUserPay(userPayment);

        // -- Handle Error
        return Result.err(Err.NO_ONE_PAYING);
    }


    // region helper method ------------------------------------------------------------------------
    private Result<PaymentDetail, Err> createUserAndMemberPay(Amount userPayment, Map<ID, Amount> memberPayment) {
        Map<ID, MonetaryAmount> memberPaymentMonetary = this.createMonetaryAmount(memberPayment);
        MonetaryAmount userPaymentMonetary = MonetaryAmount
                .create(userPayment)
                .getValue()
                .orElseThrow(ImpossibleState::new);

        return Result.ok(PaymentDetail
                .userAndMemberPay(userPaymentMonetary, memberPaymentMonetary)
                .getValue()
                .orElseThrow(ImpossibleState::new));
    }


    private Result<PaymentDetail, Err> createMemberPay(Map<ID, Amount> memberPayment) {
        Map<ID, MonetaryAmount> memberPaymentMonetary = this.createMonetaryAmount(memberPayment);

        return Result.ok(PaymentDetail
                .memberPay(memberPaymentMonetary)
                .getValue()
                .orElseThrow(ImpossibleState::new));
    }

    private Result<PaymentDetail, Err> createUserPay(Amount userPayment) {
        MonetaryAmount userPaymentMonetary = MonetaryAmount
                .create(userPayment)
                .getValue()
                .orElseThrow(ImpossibleState::new);

        return Result.ok(PaymentDetail
                .userPay(userPaymentMonetary)
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
