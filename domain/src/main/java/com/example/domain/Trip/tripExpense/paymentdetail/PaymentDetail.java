package com.example.domain.Trip.tripExpense.paymentdetail;

import com.example.domain.Shared.errorhanding.check.Check;
import com.example.domain.Shared.errorhanding.result.Result;
import com.example.domain.Shared.errorhanding.answer.Answer;
import com.example.domain.Shared.valueObject.id.ID;
import com.example.domain.Shared.valueObject.numeric.MonetaryAmount;

import java.util.Map;
import java.util.Optional;

public interface PaymentDetail {

    // region Error Class --------------------------------------------------------------------------
    public enum Err {
        ARGUMENT_NULL,
        AMOUNT_NOT_POSITIVE
    }
    // endregion Error Class -----------------------------------------------------------------------

    // region Factory method -----------------------------------------------------------------------
    static Result<PaymentDetail,Err> userPay(MonetaryAmount userPayment) {
        if (Check.isNull(userPayment)) return Result.err(Err.ARGUMENT_NULL);

        if (userPayment.isNegative()) return Result.err(Err.AMOUNT_NOT_POSITIVE);
        if (userPayment.isZero()) return Result.err(Err.AMOUNT_NOT_POSITIVE);

        return Result.ok(new UserPay(userPayment));
    }

    static Result<PaymentDetail,Err> memberPay(Map<ID, MonetaryAmount> memberPayment) {
        if (Check.isNull(memberPayment)) return Result.err(Err.ARGUMENT_NULL);

        if (Check.isEmptyMap(memberPayment)) return Result.err(Err.ARGUMENT_NULL);
        if (memberPaymentNotPositive(memberPayment)) return Result.err(Err.AMOUNT_NOT_POSITIVE);

        return Result.ok(new MemberPay(memberPayment));
    }

    static Result<PaymentDetail,Err> userAndMemberPay(MonetaryAmount userPayment, Map<ID, MonetaryAmount> memberPayment) {
        if (Check.isNull(userPayment)) return Result.err(Err.ARGUMENT_NULL);
        if (Check.isNull(memberPayment)) return Result.err(Err.ARGUMENT_NULL);

        if (userPayment.isNegative()) return Result.err(Err.AMOUNT_NOT_POSITIVE);
        if (userPayment.isZero()) return Result.err(Err.AMOUNT_NOT_POSITIVE);

        if (Check.isEmptyMap(memberPayment)) return Result.err(Err.ARGUMENT_NULL);
        if (memberPaymentNotPositive(memberPayment)) return Result.err(Err.AMOUNT_NOT_POSITIVE);

        return Result.ok(new UserAndMemberPay(userPayment,memberPayment));
    }

    static PaymentDetail unpaid() {
        return new Unpaid();
    }

    static boolean memberPaymentNotPositive(Map<ID, MonetaryAmount> memberSpending) {
        for (MonetaryAmount amount:memberSpending.values()){
            if (!amount.isPositive()) return true;
        }
        return  false;
    }
    // endregion Factory method---------------------------------------------------------------------

    enum Payer{
        USER,
        MEMBER,
        USER_AND_MEMBER,
        UNPAID
    }

    boolean isUnpaid();

    Answer<Payer> whoIsPaying();

    Optional<MonetaryAmount> getTotalPayment();

    Optional<MonetaryAmount> getUserPayment();

    Optional<MonetaryAmount> getMemberTotalPayment();

    Optional<Map<ID, MonetaryAmount>> getMemberPayment();

}
