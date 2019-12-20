package com.example.domain.Trip.tripexpense.splittingdetail;

import com.example.domain.Common.errorhanding.check.Check;
import com.example.domain.Common.errorhanding.exception.NullArgumentException;
import com.example.domain.Common.errorhanding.guard.Guard;
import com.example.domain.Common.errorhanding.result.Result;
import com.example.domain.Common.sharedvalueobject.id.ID;
import com.example.domain.Common.errorhanding.answer.Answer;
import com.example.domain.Common.sharedvalueobject.numeric.MonetaryAmount;

import java.util.Map;
import java.util.Optional;

public interface SplittingDetail {

    // region Error Class --------------------------------------------------------------------------
    public enum Err {
        ARGUMENT_NULL,
        AMOUNT_NOT_POSITIVE
    }
    // endregion Error Class -----------------------------------------------------------------------

    // region Factory method -----------------------------------------------------------------------
    static Result<SplittingDetail, Err> userSpend(MonetaryAmount userSpending) {
        if (Check.isNull(userSpending)) return Result.err(Err.ARGUMENT_NULL);

        if (userSpending.isNegative()) return Result.err(Err.AMOUNT_NOT_POSITIVE);
        if (userSpending.isZero()) return Result.err(Err.AMOUNT_NOT_POSITIVE);

        return Result.ok(new UserSpend(userSpending));
    }

    static Result<SplittingDetail, Err> memberSpend(Map<ID, MonetaryAmount> memberSpending) {
        if (Check.isNull(memberSpending)) return Result.err(Err.ARGUMENT_NULL);

        if (Check.isEmptyMap(memberSpending)) return Result.err(Err.ARGUMENT_NULL);
        if (memberSpendingNotPositive(memberSpending)) return Result.err(Err.AMOUNT_NOT_POSITIVE);

        return Result.ok(new MemberSpend(memberSpending));
    }

    static Result<SplittingDetail, Err> userAndMemberSpend(MonetaryAmount userSpending, Map<ID, MonetaryAmount> memberSpending) {
        if (Check.isNull(userSpending)) return Result.err(Err.ARGUMENT_NULL);
        if (Check.isNull(memberSpending)) return Result.err(Err.ARGUMENT_NULL);

        if (userSpending.isNegative()) return Result.err(Err.AMOUNT_NOT_POSITIVE);
        if (userSpending.isZero()) return Result.err(Err.AMOUNT_NOT_POSITIVE);

        if (Check.isEmptyMap(memberSpending)) return Result.err(Err.ARGUMENT_NULL);
        if (memberSpendingNotPositive(memberSpending)) return Result.err(Err.AMOUNT_NOT_POSITIVE);

        return Result.ok(new UserAndMemberSpend(userSpending, memberSpending));
    }

    static boolean memberSpendingNotPositive(Map<ID, MonetaryAmount> memberSpending) {
        for (MonetaryAmount amount:memberSpending.values()){
            if (!amount.isPositive()) return true;
        }
        return  false;
    }
    // endregion Factory method---------------------------------------------------------------------

    enum Spender {
        USER,
        MEMBER,
        USER_AND_MEMBER
    }

    Answer<Spender> whoIsSpending();

    MonetaryAmount getTotalSpending();

    Optional<MonetaryAmount> getUserSpending();

    Optional<MonetaryAmount> getMemberTotalSpending();

    Optional<Map<ID, MonetaryAmount>> getMemberSpending();
}
