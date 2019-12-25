package com.example.domain.Collectible.collectibleBook;

import com.example.domain.Shared.commandBaseClass.book.Book;
import com.example.domain.Shared.errorhanding.outcome.Outcome;
import com.example.domain.Trip.tripBook.TripBook;
import com.example.domain.Trip.tripExpense.TripExpense;
import com.example.domain.Shared.valueObject.id.ID;
import com.example.domain.Shared.valueObject.numeric.Amount;
import com.example.domain.Shared.valueObject.numeric.MonetaryAmount;

import java.util.Optional;

public class CollectibleRecordService {

    // region Error Class --------------------------------------------------------------------------
    public static class Err {
        public enum SaveRecord {
            SOURCE_TRANS_NOT_EXIST,
            DIFFERENT_TRIP,
            NO_BORROWING_OR_LENDING
        }

        public enum RemoveRecord {
            DIFFERENT_TRIP,
            SOURCE_STILL_EXIST
        }
    }
    // endregion Error Class -----------------------------------------------------------------------

    // ----------------------------------------- TripExpense ---------------------------------------
    public Outcome<Err.SaveRecord> saveRecord(TripBook tripBk,
                                              CollectibleBook collectibleBk,
                                              CollectibleRecordFactory factory,
                                              ID tripExpenseId) {
        // Validation
        // -- Validate Trip
        if (isDifferentTrip(tripBk, collectibleBk))
            return Outcome.err(Err.SaveRecord.DIFFERENT_TRIP);

        // -- Validate RecordSource Trans exist
        Optional<TripExpense> sourceTrans = tripBk.getTripExpenses().get(tripExpenseId);
        if (!sourceTrans.isPresent()) return Outcome.err(Err.SaveRecord.SOURCE_TRANS_NOT_EXIST);

        // -- Validate Borrowing / Lending occurs
        if (noBorrowingOrLending(sourceTrans.get()))
            return Outcome.err(Err.SaveRecord.NO_BORROWING_OR_LENDING);

        // Success
        collectibleBk.saveCollectibleRecord(factory.create(sourceTrans.get()));
        return Outcome.ok();
    }

    @Deprecated
    public Outcome<Err.RemoveRecord> removeRecord(TripBook tripBk,
                                                  CollectibleBook collectibleBk,
                                                  ID tripExpenseId) {
        // Validation
        // -- Validate Trip
        if (isDifferentTrip(tripBk, collectibleBk))
            return Outcome.err(Err.RemoveRecord.DIFFERENT_TRIP);

        // -- Validate RecordSource delete
        boolean sourceStillExist = !tripBk.getTripExpenses().contain(tripExpenseId);
        if (sourceStillExist) return Outcome.err(Err.RemoveRecord.SOURCE_STILL_EXIST);

        // Success
        collectibleBk.removeCollectibleRecord(tripExpenseId);
        return Outcome.ok();
    }


    // region helper method ------------------------------------------------------------------------
    private boolean isDifferentTrip(Book book, Book... books) {
        return !book.isSameTrip(books);
    }

    private boolean noBorrowingOrLending(TripExpense tripExpense) {
        // Prepare
        Amount userPayment = tripExpense.getPaymentDetail()
                .getUserPayment()
                .map(MonetaryAmount::getAmount)
                .orElse(Amount.createZeroAmount());
        Amount userSpending = tripExpense.getSplittingDetail()
                .getUserSpending()
                .map(MonetaryAmount::getAmount)
                .orElse(Amount.createZeroAmount());

        return userPayment.equals(userSpending);
    }
    // endregion helper method ---------------------------------------------------------------------
}
