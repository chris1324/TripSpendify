package com.example.domain.Trip.tripExpense.splittingdetail;

import com.example.domain.Shared.errorhanding.answer.Answer;
import com.example.domain.Shared.valueObject.id.ID;
import com.example.domain.Shared.valueObject.numeric.MonetaryAmount;

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
        mMemberTotalSpending = MonetaryAmount.calculator().sum(new ArrayList<>(mMemberSpending.values()));
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
