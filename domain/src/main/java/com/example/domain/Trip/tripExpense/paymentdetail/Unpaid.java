package com.example.domain.Trip.tripExpense.paymentdetail;

import com.example.domain.Shared.errorhanding.answer.Answer;
import com.example.domain.Shared.valueObject.id.ID;
import com.example.domain.Shared.valueObject.numeric.MonetaryAmount;

import java.util.Map;
import java.util.Optional;

public class Unpaid implements PaymentDetail {

    Unpaid() {
    }

    @Override
    public boolean isUnpaid() {
        return true;
    }

    @Override
    public Answer<Payer> whoIsPaying() {
        return Answer.make(Payer.UNPAID);
    }

    @Override
    public Optional<MonetaryAmount> getUserPayment() {
        return Optional.empty();
    }

    @Override
    public Optional<MonetaryAmount> getTotalPayment() {
        return Optional.empty();
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