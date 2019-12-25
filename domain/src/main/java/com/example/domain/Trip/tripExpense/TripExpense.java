package com.example.domain.Trip.tripExpense;

import com.example.domain.Trip.tripExpense.paymentdetail.PaymentDetail;
import com.example.domain.Trip.tripExpense.splittingdetail.SplittingDetail;
import com.example.domain.Shared.valueObject.id.ID;
import com.example.domain.Shared.errorhanding.exception.ImpossibleState;
import com.example.domain.Shared.errorhanding.guard.Guard;
import com.example.domain.Shared.errorhanding.exception.NullArgumentException;
import com.example.domain.Shared.errorhanding.result.Result;
import com.example.domain.Shared.valueObject.date.Date;
import com.example.domain.Shared.valueObject.numeric.MonetaryAmount;
import com.example.domain.Shared.valueObject.note.Note;
import com.example.domain.Shared.commandBaseClass.transaction.Transaction;

import java.util.ArrayList;
import java.util.List;

public class TripExpense extends Transaction {

    // region Factory method -----------------------------------------------------------------------
    public static Result<TripExpense, Err.Create> create(
            ID id,
            ID categoryId,
            Date date,
            Note note,
            MonetaryAmount totalExpense,
            PaymentDetail paymentDetail,
            SplittingDetail splittingDetail) {

        // Null checking
        try {
            Guard.NotNull(id);
            Guard.NotNull(categoryId);
            Guard.NotNull(date);
            Guard.NotNull(note);
            Guard.NotNull(totalExpense);
            Guard.NotNull(paymentDetail);
            Guard.NotNull(splittingDetail);
        } catch (NullArgumentException e) {
            return Result.err(Err.Create.NULL_ARGUMENT);
        }

        // Verify total TripExpense  = PaymentDetail
        if (!(paymentDetail.isUnpaid())) {
            MonetaryAmount totalPayment = paymentDetail
                    .getTotalPayment()
                    .orElseThrow(ImpossibleState::new);
            boolean paymentTotalNotTally = totalNotTally(totalExpense, totalPayment);
            if (paymentTotalNotTally) return Result.err(Err.Create.PAYMENT_TOTAL_NOT_TALLY);
        }

        // Verify total TripExpense  = SplitDetail
        MonetaryAmount totalSpending = splittingDetail.getTotalSpending();
        boolean splitTotalNotTally = totalNotTally(totalExpense, totalSpending);
        if (splitTotalNotTally) return Result.err(Err.Create.SPLIT_TOTAL_NOT_TALLY);

        return Result.ok(new TripExpense(
                id,
                categoryId,
                date,
                note,
                totalExpense,
                paymentDetail,
                splittingDetail));
    }

    private static boolean totalNotTally(MonetaryAmount amountA, MonetaryAmount amountB) {
        return !(amountA.equals(amountB));
    }

    // endregion Factory method---------------------------------------------------------------------

    // region Error Class --------------------------------------------------------------------------
    public static class Err {
        public enum Create {
            NULL_ARGUMENT,
            PAYMENT_TOTAL_NOT_TALLY,
            SPLIT_TOTAL_NOT_TALLY
        }
    }
    // endregion Error Class -----------------------------------------------------------------------

    // region Variables and Constructor ------------------------------------------------------------
    private final ID categoryId;
    private final PaymentDetail mPaymentDetail;
    private final SplittingDetail mSplittingDetail;

    private TripExpense(ID id,
                        ID categoryId,
                        Date date,
                        Note note,
                        MonetaryAmount totalExpense,
                        PaymentDetail paymentDetail,
                        SplittingDetail splittingDetail) {
        super(id, date, note, totalExpense);
        this.categoryId = categoryId;
        mPaymentDetail = paymentDetail;
        mSplittingDetail = splittingDetail;
    }

    // endregion Variables and Constructor ---------------------------------------------------------

    // ---------------------------------------------------------------------------------------------
    public boolean isThisMemberInvolved(ID tripMemberId) {
        if (getPayers().contains(tripMemberId)) return true;
        if (getSpenders().contains(tripMemberId)) return true;
        return false;
    }

    public List<ID> getPayers() {
        return mPaymentDetail.getMemberPayment()
                .map(memberPayment -> new ArrayList<>(memberPayment.keySet()))
                .orElseGet(ArrayList::new);
    }

    public List<ID> getSpenders() {
        return mSplittingDetail.getMemberSpending()
                .map(memberSpending -> new ArrayList<>(memberSpending.keySet()))
                .orElseGet(ArrayList::new);
    }

    // region Getter  ------------------------------------------------------------------------------
    public ID getCategoryId() {
        return categoryId;
    }

    public PaymentDetail getPaymentDetail() {
        return mPaymentDetail;
    }

    public SplittingDetail getSplittingDetail() {
        return mSplittingDetail;
    }


    // endregion Getter  ---------------------------------------------------------------------------
}
