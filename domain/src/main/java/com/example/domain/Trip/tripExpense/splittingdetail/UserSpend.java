package com.example.domain.Trip.tripExpense.splittingdetail;

import com.example.domain.Shared.errorhanding.answer.Answer;
import com.example.domain.Shared.valueObject.id.ID;
import com.example.domain.Shared.valueObject.numeric.MonetaryAmount;

import java.util.Map;
import java.util.Optional;

public class UserSpend implements SplittingDetail {

    private final MonetaryAmount mUserSpending;

    public UserSpend(MonetaryAmount userSpending) {
        mUserSpending = userSpending;
    }

    @Override
    public Answer<Spender> whoIsSpending() {
        return Answer.make(Spender.USER);
    }

    @Override
    public MonetaryAmount getTotalSpending() {
        return mUserSpending;
    }

    @Override
    public Optional<MonetaryAmount> getUserSpending() {
        return Optional.of(mUserSpending);
    }

    @Override
    public Optional<MonetaryAmount> getMemberTotalSpending() {
        return Optional.empty();
    }

    @Override
    public Optional<Map<ID, MonetaryAmount>> getMemberSpending() {
        return Optional.empty();
    }
}
