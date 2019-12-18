package com.example.domain.Trip.tripexpense.paymentdetail;

import com.example.domain.Common.errorhanding.exception.ImpossibleState;
import com.example.domain.Common.sharedvalueobject.id.ID;
import com.example.domain.Common.errorhanding.answer.Answer;
import com.example.domain.Common.sharedvalueobject.numeric.Amount;
import com.example.domain.Common.sharedvalueobject.numeric.MonetaryAmount;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

class MemberPay implements PaymentDetail {

    private final MonetaryAmount mTotalPayment;
    private final MonetaryAmount mMemberTotalPayment;
    private final Map<ID, MonetaryAmount> mMemberPayment;

    MemberPay(Map<ID, MonetaryAmount> memberPayment) {
        mMemberPayment = memberPayment;
        mMemberTotalPayment = MonetaryAmount.calculator().sum(new ArrayList<>(mMemberPayment.values()));
        mTotalPayment = mMemberTotalPayment;
    }

    @Override
    public boolean isPaid() {
        return true;
    }


    @Override
    public Answer<Payer> whoIsPaying() {
        return Answer.make(Payer.MEMBER);
    }

    @Override
    public Optional<MonetaryAmount> getTotalPayment() {
        return Optional.of(mTotalPayment);
    }

    @Override
    public Optional<MonetaryAmount> getUserPayment() {
        return Optional.empty();
    }

    @Override
    public Optional<MonetaryAmount> getMemberTotalPayment() {
        return Optional.of(mMemberTotalPayment);
    }

    @Override
    public Optional<Map<ID, MonetaryAmount>> getMemberPayment() {
        return Optional.of(Collections.unmodifiableMap(mMemberPayment));
    }

    // region helper method ------------------------------------------------------------------------
    // endregion helper method ---------------------------------------------------------------------

}