package com.example.domain.Trip.tripExpense.paymentdetail;

import com.example.domain.Shared.errorhanding.answer.Answer;
import com.example.domain.Shared.valueObject.id.ID;
import com.example.domain.Shared.valueObject.numeric.MonetaryAmount;

import java.util.Map;
import java.util.Optional;

class UserPay implements PaymentDetail {

    private final MonetaryAmount mUserPayment;

    UserPay(MonetaryAmount userPayment) {
        mUserPayment = userPayment;
    }

    @Override
    public boolean isUnpaid() {
        return false;
    }

    @Override
    public Answer<Payer> whoIsPaying() {
        return Answer.make(Payer.USER);
    }

    @Override
    public Optional<MonetaryAmount> getTotalPayment() {
        return Optional.of(mUserPayment);
    }

    @Override
    public Optional<MonetaryAmount> getUserPayment() {
        return Optional.of(mUserPayment);
    }

    @Override
    public Optional<MonetaryAmount> getMemberTotalPayment() {
        return Optional.empty();
    }

    @Override
    public Optional<Map<ID, MonetaryAmount>> getMemberPayment() {
        return Optional.empty();
    }
}