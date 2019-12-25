package com.example.domain.Trip.tripExpense.paymentdetail;

import com.example.domain.Shared.errorhanding.answer.Answer;
import com.example.domain.Shared.valueObject.id.ID;
import com.example.domain.Shared.valueObject.numeric.MonetaryAmount;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

class UserAndMemberPay implements PaymentDetail {

    private final MonetaryAmount mTotalPayment;
    private final MonetaryAmount mUserPayment;
    private final MonetaryAmount mMemberTotalPayment;
    private final Map<ID, MonetaryAmount> mMemberPayment;

    UserAndMemberPay(MonetaryAmount userPayment, Map<ID, MonetaryAmount> memberPayment) {
        mUserPayment = userPayment;
        mMemberPayment = memberPayment;
        mMemberTotalPayment = MonetaryAmount.calculator().sum(new ArrayList<>(mMemberPayment.values()));
        mTotalPayment = MonetaryAmount.calculator().sum(mUserPayment, mMemberTotalPayment);
    }

    @Override
    public boolean isUnpaid() {
        return false;
    }

    @Override
    public Answer<Payer> whoIsPaying() {
        return Answer.make(Payer.USER_AND_MEMBER);
    }

    @Override
    public Optional<MonetaryAmount> getTotalPayment() {
        return Optional.of(mTotalPayment);
    }

    @Override
    public Optional<MonetaryAmount> getUserPayment() {
        return Optional.of(mUserPayment);
    }

    @Override
    public Optional<MonetaryAmount> getMemberTotalPayment() {
        return Optional.of(mMemberTotalPayment);
    }

    @Override
    public Optional<Map<ID, MonetaryAmount>> getMemberPayment() {
        return Optional.of(Collections.unmodifiableMap(mMemberPayment));
    }
}
