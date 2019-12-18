package com.example.domain.Collectible.toremoveclass;

import com.example.domain.Collectible.collectiblebook.CollectibleBook;
import com.example.domain.Collectible.collectiblerecord.CollectibleRecord;
import com.example.domain.Collectible.collectibletransaction.CollectibleTransaction;
import com.example.domain.Common.baseclass.book.Book;
import com.example.domain.Common.errorhanding.outcome.Outcome;
import com.example.domain.Trip.tripbook.TripBook;
import com.example.domain.Trip.tripexpense.TripExpense;

import java.util.Optional;

public class AddCollectibleRecordService {

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
        if (invalidSource(record, CollectibleRecord.Source.TRIP_EXPENSE)) return Outcome.err(Err.TripExpense.INVALID_SOURCE_TYPE);

        // -- Validate Source Trans exist
        Optional<TripExpense> sourceTrans = tripBk.getTripExpenses().get(record.getSourceTransId());
        if (!sourceTrans.isPresent()) return Outcome.err(Err.TripExpense.SOURCE_TRANS_NOT_EXIST);

        // -- Validate Borrowing / Lending occurs
        // FIXME: 18/12/2019 Pass it to Factory
        if (noBorrowingOrLending(sourceTrans.get()))
            return Outcome.err(Err.TripExpense.NO_BORROWING_OR_LENDING);

        // FIXME: 18/12/2019 NOW!
        //collectibleBk.addCollectibleRecord(record);
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

        // FIXME: 18/12/2019 NOW!
        //collectibleBk.addCollectibleTrans(transaction);
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

//        Amount userPayment = tripExpense.getPaymentDetail()
//                .getUserPayment()
//                .map(monetaryAmount -> (Amount) monetaryAmount)
//                .orElse(Amount.createZeroAmount());
//        Amount userSpending = tripExpense.getSplittingDetail()
//                .getUserSpending()
//                .map(monetaryAmount -> (Amount) monetaryAmount)
//                .orElse(Amount.createZeroAmount());
//
//        final Amount test = userPayment.test(userPayment);
//        return userPayment.equals(userSpending);
        // FIXME: 18/12/2019 NOW!
        return true;
    }
    // endregion helper method ---------------------------------------------------------------------
}
