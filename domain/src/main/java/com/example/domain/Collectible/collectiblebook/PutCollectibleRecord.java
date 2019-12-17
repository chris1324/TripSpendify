package com.example.domain.Collectible.collectiblebook;

import com.example.domain.Collectible.collectiblerecord.CollectibleRecord;
import com.example.domain.Collectible.collectibletransaction.CollectibleTransaction;
import com.example.domain.Common.entity.book.Book;
import com.example.domain.Common.errorhanding.outcome.Outcome;
import com.example.domain.Common.sharedvalueobject.amount.Amount;
import com.example.domain.Common.sharedvalueobject.amount.MonetaryAmount;
import com.example.domain.Trip.tripbook.TripBook;
import com.example.domain.Trip.tripexpense.TripExpense;

import java.util.Optional;
import java.util.function.Function;

public class PutCollectibleRecord {

    // region Error Class --------------------------------------------------------------------------
    public static class Err {
        public enum TripExpense {
            INVALID_SOURCE_TYPE,
            SOURCE_TRANS_NOT_EXIST,
            DIFFERENT_TRIP,
            NO_BORROWING_OR_LENDING
        }

        public enum CollectibleTrans {
            INVALID_MEMBER,
            DIFFERENT_TRIP
        }
    }
    // endregion Error Class -----------------------------------------------------------------------

    // ----------------------------------------- TripExpense ---------------------------------------
    public Outcome<Err.TripExpense> fromTripExpense(TripBook tripBk,
                                                    CollectibleBook collectibleBk,
                                                    CollectibleRecord record) {
        // Validation
        // -- Validate Trip
        if (isDifferentTrip(tripBk, collectibleBk))
            return Outcome.err(Err.TripExpense.DIFFERENT_TRIP);

        // -- Validate Source Type
        if (invalidSource(record, CollectibleRecord.Source.TRIP_EXPENSE))
            return Outcome.err(Err.TripExpense.INVALID_SOURCE_TYPE);

        // -- Validate Source Trans exist
        Optional<TripExpense> sourceTrans = tripBk.getTripExpenses().get(record.getSourceTransId());
        if (!sourceTrans.isPresent()) return Outcome.err(Err.TripExpense.SOURCE_TRANS_NOT_EXIST);

        // -- Validate Borrowing / Lending occurs
        if (noBorrowingOrLending(sourceTrans.get()))
            return Outcome.err(Err.TripExpense.NO_BORROWING_OR_LENDING);

        collectibleBk.putCollectibleRecord(record);
        return Outcome.ok();
    }


    // ------------------------------------Collectible Transaction----------------------------------
    public Outcome<Err.CollectibleTrans> fromCollectibleTrans(TripBook tripBk,
                                                              CollectibleBook collectibleBk,
                                                              CollectibleTransaction transaction) {
        // Validation
        // -- Validate Trip
        if (isDifferentTrip(tripBk, collectibleBk))
            return Outcome.err(Err.CollectibleTrans.DIFFERENT_TRIP);

        // -- Validate Member
        if (isInvalidMember(tripBk, transaction))
            return Outcome.err(Err.CollectibleTrans.INVALID_MEMBER);

        collectibleBk.putCollectibleTrans(transaction);
        return Outcome.ok();
    }


    // region helper method ------------------------------------------------------------------------
    private boolean invalidSource(CollectibleRecord record, CollectibleRecord.Source source) {
        return record.getSource() != source;
    }

    private boolean isDifferentTrip(Book book, Book... books) {
        return !book.isSameTrip(books);
    }

    private boolean isInvalidMember(TripBook tripBk, CollectibleTransaction transaction) {
        return !tripBk.getTripMembers().contain(transaction.getInvolvedMemberId());
    }

    private boolean noBorrowingOrLending(TripExpense tripExpense) {
        if (!tripExpense.getPaymentDetail().isPaid()) return true;

        Amount userPayment = tripExpense.getPaymentDetail()
                .getUserPayment()
                .map(monetaryAmount -> (Amount) monetaryAmount)
                .orElse(Amount.createZeroAmount());
        Amount userSpending = tripExpense.getSplittingDetail()
                .getUserSpending()
                .map(monetaryAmount -> (Amount) monetaryAmount)
                .orElse(Amount.createZeroAmount());

        return userPayment.equals(userSpending);
    }
    // endregion helper method ---------------------------------------------------------------------
}
