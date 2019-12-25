package com.example.domain.Collectible.collectibleBook;

import com.example.domain.Collectible.collectibleTransaction.CollectibleTransaction;
import com.example.domain.Shared.commandBaseClass.book.Book;
import com.example.domain.Shared.errorhanding.outcome.Outcome;
import com.example.domain.Trip.tripBook.TripBook;
import com.example.domain.Shared.valueObject.id.ID;

public class CollectibleTransactionService {

    // region Error Class --------------------------------------------------------------------------
    public static class Err {
        public enum SaveCollTrans {
            MEMBER_INVALID,
            TRIP_DIFFERENT
        }
    }
    // endregion Error Class -----------------------------------------------------------------------

    // ------------------------------------Collectible Transaction----------------------------------
    public Outcome<Err.SaveCollTrans> saveCollTrans(TripBook tripBk,
                                                    CollectibleBook collectibleBk,
                                                    CollectibleRecordFactory factory,
                                                    CollectibleTransaction transaction) {
        // Validation
        // -- Validate Trip
        if (isDifferentTrip(tripBk, collectibleBk))
            return Outcome.err(Err.SaveCollTrans.TRIP_DIFFERENT);

        // -- Validate Member
        if (isInvalidMember(tripBk, transaction))
            return Outcome.err(Err.SaveCollTrans.MEMBER_INVALID);

        // Success
        collectibleBk.saveCollectibleTrans(transaction, factory.create(transaction));
        return Outcome.ok();
    }


    public void removeCollTrans(CollectibleBook collectibleBk, ID collectibleTransId) {
        collectibleBk.removeCollectibleTrans(collectibleTransId);
    }

    // region helper method ------------------------------------------------------------------------
    private boolean isDifferentTrip(Book book, Book... books) {
        return !book.isSameTrip(books);
    }

    private boolean isInvalidMember(TripBook tripBk, CollectibleTransaction transaction) {
        return !tripBk.getTripMembers().contain(transaction.getInvolvedMemberId());
    }
    // endregion helper method ---------------------------------------------------------------------
}
