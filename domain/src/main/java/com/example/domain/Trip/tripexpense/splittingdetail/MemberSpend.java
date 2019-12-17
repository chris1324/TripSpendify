package com.example.domain.Trip.tripexpense.splittingdetail;

import com.example.domain.Common.calculator.Calculator;
import com.example.domain.Common.entity.ID;
import com.example.domain.Common.errorhanding.answer.Answer;
import com.example.domain.Common.sharedvalueobject.amount.MonetaryAmount;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

public class MemberSpend implements SplittingDetail {

    private final MonetaryAmount mTotalSpending;
    private final MonetaryAmount mMemberTotalSpending;
    private final Map<ID, MonetaryAmount> mMemberSpending;

    public MemberSpend(Map<ID, MonetaryAmount> memberSpending) {
        mMemberSpending = memberSpending;
        mMemberTotalSpending= Calculator.get().sum(new ArrayList<>(mMemberSpending.values()));
        mTotalSpending = mMemberTotalSpending;
    }

    @Override
    public Answer<Spender> whoIsSpending() {
        return Answer.make(Spender.MEMBER);
    }

    @Override
    public MonetaryAmount getTotalSpending() {
        return mTotalSpending;
    }

    @Override
    public Optional<MonetaryAmount> getUserSpending() {
        return Optional.empty();
    }

    @Override
    public Optional<MonetaryAmount> getMemberTotalSpending() {
        return Optional.of(mMemberTotalSpending);
    }

    @Override
    public Optional<Map<ID, MonetaryAmount>> getMemberSpending() {
        return Optional.of(Collections.unmodifiableMap(mMemberSpending));
    }
}
