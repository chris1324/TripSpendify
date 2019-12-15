package com.example.domain.Trip.tripexpense.splittingdetail;

import com.example.domain.Common.calculator.Calculator;
import com.example.domain.Common.entity.ID;
import com.example.domain.Common.errorhanding.answer.Answer;
import com.example.domain.Common.sharedvalueobject.monetaryamount.MonetaryAmount;

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

        Calculator calculator = Calculator.get();
        mMemberTotalSpending= calculator.sum(new ArrayList<>(mMemberSpending.values()));
        mTotalSpending = calculator.sum(mUserSpending, mMemberTotalSpending);
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
