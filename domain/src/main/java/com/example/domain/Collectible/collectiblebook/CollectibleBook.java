package com.example.domain.Collectible.collectiblebook;

import com.example.domain.Collectible.collectiblerecord.CollectibleRecord;
import com.example.domain.Collectible.collectibletransaction.CollectibleTransaction;

import com.example.domain.Collectible.domainevent.CollTransAdded;
import com.example.domain.Common.baseclass.transaction.Transaction;
import com.example.domain.Common.sharedvalueobject.id.ID;
import com.example.domain.Common.baseclass.book.Book;
import com.example.domain.Common.entitylist.EntityList;
import com.example.domain.Common.errorhanding.exception.NullArgumentException;
import com.example.domain.Common.errorhanding.guard.Guard;
import com.example.domain.Common.errorhanding.result.Result;

import java.util.List;

public class CollectibleBook extends Book {

    // region Factory method -----------------------------------------------------------------------
    public static Result<CollectibleBook, Err.Create> create(ID id,
                                                             ID tripBookID,
                                                             List<CollectibleRecord> collectibleRecords,
                                                             List<CollectibleTransaction> collectibleTrans) {
        try {
            Guard.NotNull(id);
            Guard.NotNull(tripBookID);
            Guard.NotNull(collectibleTrans);
        } catch (NullArgumentException e) {
            return Result.err(Err.Create.NULL_ARGUMENT);
        }

        return Result.ok(new CollectibleBook(
                id,
                tripBookID,
                EntityList.newList(collectibleRecords),
                EntityList.newList(collectibleTrans)));
    }
    // endregion Factory method---------------------------------------------------------------------

    // region Error Class --------------------------------------------------------------------------
    public static class Err {
        public enum Create {
            NULL_ARGUMENT
        }
    }
    // endregion Error Class -----------------------------------------------------------------------

    // region Variables and Constructor ------------------------------------------------------------
    private final EntityList<CollectibleRecord> mCollectibleRecords;
    private final EntityList<CollectibleTransaction> mCollectibleTrans;

    public CollectibleBook(ID id,
                           ID tripBookID,
                           EntityList<CollectibleRecord> collectibleRecords,
                           EntityList<CollectibleTransaction> collectibleTrans) {
        super(id, tripBookID);
        mCollectibleRecords = collectibleRecords;
        mCollectibleTrans = collectibleTrans;
    }

    // endregion Variables and Constructor ---------------------------------------------------------

    // ----------------------------------Collectible Record ----------------------------------------
    void addCollectibleRecord(CollectibleRecord record) {
        mCollectibleRecords.put(record);
    }

    void removeCollectibleRecord(ID sourceTransId) {
        mCollectibleRecords.remove(record -> record.getSourceTransId().equals(sourceTransId));
    }

    // ----------------------------------Collectible Transaction -----------------------------------
    void addCollectibleTrans(CollectibleTransaction transaction, CollectibleRecord record) {
        mCollectibleTrans.put(transaction);
        this.addCollectibleRecord(record);
        super.addDomainEvent(new CollTransAdded(this.getTripBookID(), transaction.getId()));
    }

    void removeCollectibleTrans(ID transactionID) {
        mCollectibleTrans.remove(transactionID);
        this.removeCollectibleRecord(transactionID);
        super.addDomainEvent(new CollTransAdded(this.getTripBookID(), transactionID));
    }

    // region Getter -------------------------------------------------------------------------------
    public EntityList<CollectibleTransaction> getCollectibleTrans() {
        return mCollectibleTrans.unmodifiable();
    }

    public EntityList<CollectibleRecord> getCollectibleRecords() {
        return mCollectibleRecords.unmodifiable();
    }
    // endregion Getter ----------------------------------------------------------------------------
}
