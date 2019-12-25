package com.example.domain.Cash.cashBook;

import com.example.domain.Cash.cashTransaction.CashTransaction;
import com.example.domain.Shared.commandBaseClass.book.Book;
import com.example.domain.Shared.errorhanding.outcome.Outcome;
import com.example.domain.Trip.tripBook.TripBook;
import com.example.domain.Shared.valueObject.date.Date;
import com.example.domain.Shared.valueObject.id.ID;

public class CashTransactionService {

    // region Error Class --------------------------------------------------------------------------

    public static class Err {
        public enum CashTrans {
            TRIP_DIFFERENT,
            DATE_INVALID
        }
    }

    // endregion Error Class -----------------------------------------------------------------------
    public Outcome<Err.CashTrans> saveCashTrans(TripBook tripBk,
                                                CashBook cashBk,
                                                CashRecordFactory factory,
                                                CashTransaction cashTrans) {
        // Validation
        // -- Validate Trip & Date
        if (isDifferentTrip(tripBk, cashBk))
            return Outcome.err(Err.CashTrans.TRIP_DIFFERENT);
        if (notDuringTrip(tripBk, cashTrans.getDate()))
            return Outcome.err(Err.CashTrans.DATE_INVALID);

        // Success
        cashBk.saveCashTransaction(cashTrans, factory.create(cashTrans));
        return Outcome.ok();
    }

    public void removeCashTrans(CashBook cashBook, ID cashTransId) {
        cashBook.removeCashTransaction(cashTransId);
    }

    // region helper method ------------------------------------------------------------------------
    private boolean notDuringTrip(TripBook tripBk, Date transDate) {
        return tripBk.isDuringTrip(transDate);
    }

    private boolean isDifferentTrip(Book book, Book... books) {
        return !book.isSameTrip(books);
    }

    // endregion helper method ---------------------------------------------------------------------
}
