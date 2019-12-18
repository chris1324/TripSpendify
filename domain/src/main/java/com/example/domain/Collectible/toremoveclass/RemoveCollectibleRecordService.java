package com.example.domain.Collectible.toremoveclass;

import com.example.domain.Collectible.collectiblebook.CollectibleBook;
import com.example.domain.Common.sharedvalueobject.id.ID;
import com.example.domain.Common.baseclass.book.Book;
import com.example.domain.Common.errorhanding.outcome.Outcome;
import com.example.domain.Trip.tripbook.TripBook;

public class RemoveCollectibleRecordService {

    // region Error Class --------------------------------------------------------------------------
    public static enum Err {
        DIFFERENT_TRIP,
        SOURCE_STILL_EXIST,
    }
    // endregion Error Class -----------------------------------------------------------------------

    // ----------------------------------------- TripExpense ---------------------------------------
    public Outcome<Err> withTripExpenseId(TripBook tripBk, CollectibleBook collectibleBk, ID tripExpenseId){
        // Validation
        // -- Validate Trip
        if (isDifferentTrip(tripBk, collectibleBk)) return Outcome.err(Err.DIFFERENT_TRIP);

        // -- Validate Source delete
        boolean sourceStillExist = !tripBk.getTripExpenses().contain(tripExpenseId);
        if (sourceStillExist) return Outcome.err(Err.SOURCE_STILL_EXIST);

        // Success
        //collectibleBk.removeCollectibleRecord(tripExpenseId);
        return Outcome.ok();
    }

    // ------------------------------------Collectible Transaction----------------------------------

    public Outcome<Err> withCollectibleTransId( CollectibleBook collectibleBk, ID collectibleTransId){
        //collectibleBk.removeCollectibleTrans(collectibleTransId);
        return Outcome.ok();
    }

    // region helper method ------------------------------------------------------------------------
    private boolean isDifferentTrip(Book book, Book... books) {
        for (Book b : books) {
            if (!book.getTripBookID().equals(b.getTripBookID())) return true;
        }
        return false;
    }
    // endregion helper method ---------------------------------------------------------------------

}
