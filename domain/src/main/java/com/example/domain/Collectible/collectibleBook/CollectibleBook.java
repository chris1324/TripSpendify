package com.example.domain.Collectible.collectibleBook;

import com.example.domain.Collectible.collectibleRecord.CollectibleRecord;
import com.example.domain.Collectible.collectibleTransaction.CollectibleTransaction;

import com.example.domain.Collectible.collectibleEvent.CollTransSaved;
import com.example.domain.Shared.entityList.EntityList;
import com.example.domain.Shared.errorhanding.exception.NullArgumentException;
import com.example.domain.Shared.errorhanding.guard.Guard;
import com.example.domain.Shared.errorhanding.result.Result;
import com.example.domain.Shared.valueObject.id.ID;
import com.example.domain.Shared.commandBaseClass.book.Book;

import java.util.List;

public class CollectibleBook extends Book {

    // region Factory method -----------------------------------------------------------------------
    public static Result<CollectibleBook, Err.Create> create(ID tripBookID,
                                                             List<CollectibleRecord> collectibleRecords,
                                                             List<CollectibleTransaction> collectibleTrans) {
        try {
            Guard.NotNull(tripBookID);
            Guard.NotNull(collectibleRecords);
            Guard.NotNull(collectibleTrans);
        } catch (NullArgumentException e) {
            return Result.err(Err.Create.NULL_ARGUMENT);
        }

        return Result.ok(new CollectibleBook(
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

    public CollectibleBook(ID tripBookID,
                           EntityList<CollectibleRecord> collectibleRecords,
                           EntityList<CollectibleTransaction> collectibleTrans) {
        super(tripBookID);
        mCollectibleRecords = collectibleRecords;
        mCollectibleTrans = collectibleTrans;
    }

    // endregion Variables and Constructor ---------------------------------------------------------

    // ----------------------------------Collectible Record ----------------------------------------
    void saveCollectibleRecord(CollectibleRecord record) {
        mCollectibleRecords.put(record);
    }

    @Deprecated
    void removeCollectibleRecord(ID sourceTransId) {
        mCollectibleRecords.remove(record -> record.getSourceTransId().equals(sourceTransId));
    }

    // ----------------------------------Collectible Transaction -----------------------------------
    void saveCollectibleTrans(CollectibleTransaction transaction, CollectibleRecord record) {
        mCollectibleTrans.put(transaction);
        this.saveCollectibleRecord(record);
        super.addDomainEvent(new CollTransSaved(this.getTripID(), transaction.getId()));
    }

    void removeCollectibleTrans(ID transactionID) {
        mCollectibleTrans.remove(transactionID);
        this.removeCollectibleRecord(transactionID);
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
