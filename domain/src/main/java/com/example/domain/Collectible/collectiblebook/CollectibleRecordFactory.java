package com.example.domain.Collectible.collectiblebook;

import com.example.domain.Collectible.collectiblerecord.CollectibleRecord;
import com.example.domain.Collectible.collectibletransaction.CollectibleTransaction;
import com.example.domain.Common.errorhanding.exception.ImpossibleState;
import com.example.domain.Common.errorhanding.exception.UnexpectedEnumValue;
import com.example.domain.Common.errorhanding.exception.UnhandledError;
import com.example.domain.Common.errorhanding.pair.Pair;
import com.example.domain.Common.sharedvalueobject.id.ID;
import com.example.domain.Common.sharedvalueobject.numeric.Amount;
import com.example.domain.Common.sharedvalueobject.numeric.MonetaryAmount;
import com.example.domain.Trip.tripexpense.TripExpense;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CollectibleRecordFactory {

    // region Error Class --------------------------------------------------------------------------
    public enum Err {

    }
    // endregion Error Class -----------------------------------------------------------------------

    // ------------------------------------Collectible Transaction----------------------------------
    CollectibleRecord create(CollectibleTransaction trans) {
        // Prepare
        Map<ID, MonetaryAmount> involvedMemberDetail = new HashMap<>();
        involvedMemberDetail.put(trans.getInvolvedMemberId(), trans.getAmount());

        // Create and Return
        return CollectibleRecord
                .create(
                        trans.getId(),
                        this.getSourceFromCollTrans(trans),
                        involvedMemberDetail
                )
                .getValue()
                .orElseThrow(ImpossibleState::new);
    }

    // region helper method ------------------------------------------------------------------------
    private CollectibleRecord.Source getSourceFromCollTrans(CollectibleTransaction transaction) {
        // Prepare
        CollectibleTransaction.Type transactionType = transaction.getTransactionType();
        boolean userIsPayer = transaction.isUserPayer();

        // Compare and Return
        switch (transactionType) {
            case CONTRIBUTION:
                if (userIsPayer) return CollectibleRecord.Source.COLL_CONTRIBUTION_MADE;
                return CollectibleRecord.Source.COLL_CONTRIBUTION_ACCEPTED;
            case SETTLEMENT:
                if (userIsPayer) return CollectibleRecord.Source.COLL_SETTLEMENT_MADE;
                return CollectibleRecord.Source.COLL_SETTLEMENT_ACCEPTED;
            default:
                throw new UnexpectedEnumValue();
        }
    }
    // endregion helper method ---------------------------------------------------------------------

    // ----------------------------------------- TripExpense ---------------------------------------
    CollectibleRecord create(TripExpense tripExpense) {
        // Get Lender & Borrower
        Map<ID, Amount> borrowerList = this.getBorrower(tripExpense);
        Map<ID, Amount> lenderList = this.getLender(tripExpense);

        // Calculate amount borrowed/lent by Members
        Amount amountBorrowedByMembers = Amount.calculator().sum(new ArrayList<>(borrowerList.values()));
        Amount amountLentByMembers = Amount.calculator().sum(new ArrayList<>(lenderList.values()));

        // Check if user is lending/borrowing
        Pair<Boolean, Amount> result = this.isUserLending(tripExpense);
        boolean userIsLending = result.getLeft();

        // Delegate and Return
        if (userIsLending) return createWithUserLending(
                tripExpense,
                borrowerList,
                result.getRight(),
                amountLentByMembers
        );

        else return createWithUserBorrowing(
                tripExpense,
                lenderList,
                result.getRight(),
                amountBorrowedByMembers
        );
    }

    // region helper method ------------------------------------------------------------------------
    private CollectibleRecord createWithUserLending(TripExpense tripExpense,
                                                    Map<ID, Amount> borrowerList,
                                                    Amount amountLentByUser,
                                                    Amount amountLentByMembers) {
        // Prepare
        Map<ID, MonetaryAmount> borrowerDetail = new HashMap<>();

        // Calculate % from User
        Amount portionLendByUser = this.getUserPortion(
                amountLentByUser,
                amountLentByMembers);

        // Calculate $ lend by User for each borrower
        for (ID borrowerId : borrowerList.keySet()) {
            Amount borrowing = borrowerList.get(borrowerId);

            Amount portionFromUser = Amount.calculator()
                    .multiply(
                            borrowing,
                            portionLendByUser);

            borrowerDetail.put(
                    borrowerId,
                    MonetaryAmount
                            .create(portionFromUser)
                            .getValue()
                            .orElseThrow(ImpossibleState::new));
        }

        // Create and Return
        return CollectibleRecord
                .create(
                        tripExpense.getId(),
                        CollectibleRecord.Source.TRIP_EXPENSE_LENDING,
                        borrowerDetail)
                .getValue()
                .orElseThrow(ImpossibleState::new);
    }


    private CollectibleRecord createWithUserBorrowing(TripExpense tripExpense,
                                                      Map<ID, Amount> lenderList,
                                                      Amount amountBorrowedByUser,
                                                      Amount amountBorrowedByMembers) {
        // Prepare
        Map<ID, MonetaryAmount> lenderDetail = new HashMap<>();

        // Calculate % from User
        Amount portionBorrowedByUser = this.getUserPortion(
                amountBorrowedByUser,
                amountBorrowedByMembers);

        // Calculate $ borrower by User from each borrower
        for (ID lenderId : lenderList.keySet()) {
            Amount lending = lenderList.get(lenderId);

            Amount portionFromUser = Amount.calculator()
                    .multiply(
                            lending,
                            portionBorrowedByUser);

            lenderDetail.put(
                    lenderId,
                    MonetaryAmount
                            .create(portionFromUser)
                            .getValue()
                            .orElseThrow(ImpossibleState::new));
        }

        // Create and Return
        return CollectibleRecord
                .create(
                        tripExpense.getId(),
                        CollectibleRecord.Source.TRIP_EXPENSE_LENDING,
                        lenderDetail)
                .getValue()
                .orElseThrow(ImpossibleState::new);
    }

    private Pair<Boolean, Amount> isUserLending(TripExpense tripExpense) {
        // Prepare
        Amount userPayment = tripExpense.getPaymentDetail()
                .getUserPayment()
                .map(MonetaryAmount::getAmount)
                .orElse(Amount.createZeroAmount());
        Amount userSpending = tripExpense.getSplittingDetail()
                .getUserSpending()
                .map(MonetaryAmount::getAmount)
                .orElse(Amount.createZeroAmount());

        // Calculate
        Amount amountLend = Amount.calculator().subtract(userPayment, userSpending);

        // Compare and Return
        if (amountLend.isPositive()) return Pair.make(true, amountLend);
        else return Pair.make(false, amountLend.abs());
    }

    private Map<ID, Amount> getLender(TripExpense trans) {
        // Prepare
        Map<ID, MonetaryAmount> spendingMember = trans.getSplittingDetail().getMemberSpending().orElse(new HashMap<>());
        Map<ID, MonetaryAmount> payingMember = trans.getPaymentDetail().getMemberPayment().orElse(new HashMap<>());
        Map<ID, Amount> lenderList = new HashMap<>();

        // Calculate
        for (ID payerId : payingMember.keySet()) {
            Amount spentAmount = spendingMember.get(payerId).getAmount();
            Amount paidAmount = payingMember.get(payerId).getAmount();

            // He pay but didn't spend -> Lender
            if (spentAmount == null) {
                lenderList.put(payerId, paidAmount);
                break;
            }

            // He pay more then he spend -> Lender
            Amount amountLent = Amount.calculator().subtract(
                    paidAmount,
                    spentAmount
            );

            if (amountLent.isPositive()) lenderList.put(payerId, amountLent);
        }

        // Return
        return lenderList;
    }

    private Map<ID, Amount> getBorrower(TripExpense trans) {
        // Prepare
        Map<ID, MonetaryAmount> spendingMember = trans.getSplittingDetail().getMemberSpending().orElse(new HashMap<>());
        Map<ID, MonetaryAmount> payingMember = trans.getPaymentDetail().getMemberPayment().orElse(new HashMap<>());
        Map<ID, Amount> borrowerList = new HashMap<>();

        // Calculate
        for (ID spenderId : spendingMember.keySet()) {
            Amount spentAmount = spendingMember.get(spenderId).getAmount();
            Amount paidAmount = payingMember.get(spenderId).getAmount();

            // He spend but didn't pay -> Borrower
            if (paidAmount == null) {
                borrowerList.put(spenderId, spentAmount);
                break;
            }

            // He spend more than he pay -> Borrower
            Amount borrowedAmount = Amount.calculator().subtract(
                    spentAmount,
                    paidAmount
            );

            if (borrowedAmount.isPositive()) borrowerList.put(spenderId, borrowedAmount);
        }

        // Return
        return borrowerList;
    }

    private Amount getUserPortion(Amount fromUser, Amount fromMember) {
        Amount total = Amount.calculator().sum(fromUser, fromMember);
        return Amount.calculator().divide(fromMember, total);
    }
    // endregion helper method ---------------------------------------------------------------------
}
