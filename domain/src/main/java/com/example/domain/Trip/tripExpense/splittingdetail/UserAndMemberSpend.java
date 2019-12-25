package com.example.domain.Trip.tripExpense.splittingdetail;

import com.example.domain.Shared.errorhanding.answer.Answer;
import com.example.domain.Shared.valueObject.id.ID;
import com.example.domain.Shared.valueObject.numeric.MonetaryAmount;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

public class UserAndMemberSpend implements SplittingDetail {

    private final MonetaryAmount mUserSpending;
    private final MonetaryAmount mTotalSpending;
    private final MonetaryAmount mMemberTotalSpending;
    private final Map<ID, MonetaryAmount> mMemberSpending;

    public UserAndMemberSpend(MonetaryAmount userSpending, Map<ID, MonetaryAmount> memberSpending) {
        mUserSpending = userSpending;
        mMemberSpending = memberSpending;
        mMemberTotalSpending = MonetaryAmount.calculator().sum(new ArrayList<>(mMemberSpending.values()));
        mTotalSpending = MonetaryAmount.calculator().sum(mUserSpending, mMemberTotalSpending);
    }

    @Override
    public Answer<Spender> whoIsSpending() {
        return Answer.make(Spender.USER_AND_MEMBER);
    }

    @Override
    public MonetaryAmount getTotalSpending() {
        return mTotalSpending;
    }

    @Override
    public Optional<MonetaryAmount> getUserSpending() {
        return Optional.of(mUserSpending);
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
