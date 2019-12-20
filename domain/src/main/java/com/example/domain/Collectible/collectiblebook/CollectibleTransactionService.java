package com.example.domain.Collectible.collectiblebook;

import com.example.domain.Collectible.collectibletransaction.CollectibleTransaction;
import com.example.domain.Common.baseclass.book.Book;
import com.example.domain.Common.errorhanding.outcome.Outcome;
import com.example.domain.Common.sharedvalueobject.id.ID;
import com.example.domain.Trip.tripbook.TripBook;

public class CollectibleTransactionService {

    // region Error Class --------------------------------------------------------------------------
    public static class Err {
        public enum AddCollTrans {
            MEMBER_INVALID,
            TRIP_DIFFERENT
        }
    }
    // endregion Error Class -----------------------------------------------------------------------

    // ------------------------------------Collectible Transaction----------------------------------
    public Outcome<Err.AddCollTrans> addCollTrans(TripBook tripBk,
                                                  CollectibleBook collectibleBk,
                                                  CollectibleRecordFactory factory,
                                                  CollectibleTransaction transaction) {
        // Validation
        // -- Validate Trip
        if (isDifferentTrip(tripBk, collectibleBk))
            return Outcome.err(Err.AddCollTrans.TRIP_DIFFERENT);

        // -- Validate Member
        if (isInvalidMember(tripBk, transaction))
            return Outcome.err(Err.AddCollTrans.MEMBER_INVALID);

        // Success
        collectibleBk.addCollectibleTrans(transaction, factory.create(transaction));
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
