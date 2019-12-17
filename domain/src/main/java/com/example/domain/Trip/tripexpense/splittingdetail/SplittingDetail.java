package com.example.domain.Trip.tripexpense.splittingdetail;

import com.example.domain.Common.entity.ID;
import com.example.domain.Common.errorhanding.answer.Answer;
import com.example.domain.Common.sharedvalueobject.amount.MonetaryAmount;

import java.util.Map;
import java.util.Optional;

public interface SplittingDetail {

    enum Spender {
        USER,
        MEMBER,
        USER_AND_MEMBER
    }

    static SplittingDetail userSpend(MonetaryAmount userSpending) {
        return new UserSpend(userSpending);
    }

    static SplittingDetail memberSpend(Map<ID, MonetaryAmount> memberSpending) {
        return new MemberSpend(memberSpending);
    }

    static SplittingDetail userAndMemberSpend(MonetaryAmount userSpending, Map<ID, MonetaryAmount> memberSpending) {
        return new UserAndMemberSpend(userSpending, memberSpending);
    }

    Answer<Spender> whoIsSpending();

    MonetaryAmount getTotalSpending();

    Optional<MonetaryAmount> getUserSpending();

    Optional<MonetaryAmount> getMemberTotalSpending();

    Optional<Map<ID, MonetaryAmount>> getMemberSpending();
}
