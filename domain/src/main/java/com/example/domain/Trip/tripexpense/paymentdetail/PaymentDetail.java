package com.example.domain.Trip.tripexpense.paymentdetail;

import com.example.domain.Common.entity.ID;
import com.example.domain.Common.errorhanding.answer.Answer;
import com.example.domain.Common.sharedvalueobject.monetaryamount.MonetaryAmount;

import java.util.Map;
import java.util.Optional;

public interface PaymentDetail {

    enum Payer{
        USER,
        MEMBER,
        USER_AND_MEMBER,
        UNPAID
    }

    static PaymentDetail userPay(MonetaryAmount userPayment) {
        return new UserPay(userPayment);
    }

    static PaymentDetail memberPay(Map<ID, MonetaryAmount> memberPayment) {
        return new MemberPay(memberPayment);
    }

    static PaymentDetail userAndMemberPay(MonetaryAmount userPayment, Map<ID, MonetaryAmount> memberPayment) {
        return new UserAndMemberPay(userPayment,memberPayment);
    }

    static PaymentDetail unpaid() {
        return new Unpaid();
    }

    boolean isPaid();

    Answer<Payer> whoIsPaying();

    Optional<MonetaryAmount> getTotalPayment();

    Optional<MonetaryAmount> getUserPayment();

    Optional<MonetaryAmount> getMemberTotalPayment();

    Optional<Map<ID, MonetaryAmount>> getMemberPayment();

}
