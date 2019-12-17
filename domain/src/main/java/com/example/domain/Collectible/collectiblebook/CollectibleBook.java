package com.example.domain.Collectible.collectiblebook;

import com.example.domain.Collectible.collectiblerecord.CollectibleRecord;
import com.example.domain.Collectible.collectibletransaction.CollectibleTransaction;

import com.example.domain.Common.entity.ID;
import com.example.domain.Common.entity.book.Book;
import com.example.domain.Common.entitylist.EntityList;
import com.example.domain.Common.errorhanding.exception.ImpossibleState;
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
    void putCollectibleRecord(CollectibleRecord record) {
        mCollectibleRecords.put(record);
    }

    void removeCollectibleRecord(ID sourceTransId) {
        mCollectibleRecords.remove(record -> record.getSourceTransId().equals(sourceTransId));
    }

    // ----------------------------------Collectible Transaction -----------------------------------
    void putCollectibleTrans(CollectibleTransaction transaction) {
        mCollectibleTrans.put(transaction);
        this.putCollectibleRecord(createRecordFromCollTrans(transaction));
    }

    void removeCollectibleTrans(ID transactionID) {
        mCollectibleTrans.remove(transactionID);
        this.removeCollectibleRecord(transactionID);
    }

    // region helper method ------------------------------------------------------------------------
    private CollectibleRecord createRecordFromCollTrans(CollectibleTransaction transaction) {
        return CollectibleRecord
                .creare(
                        ID.newId(),
                        CollectibleRecord.Source.COLLECTIBLE_TRANS,
                        transaction.getId()
                )
                .getValue()
                .orElseThrow(ImpossibleState::new);
    }
    // endregion helper method ---------------------------------------------------------------------

    // region Getter -------------------------------------------------------------------------------
    public EntityList<CollectibleTransaction> getCollectibleTrans() {
        return mCollectibleTrans.unmodifiable();
    }

    public EntityList<CollectibleRecord> getCollectibleRecords() {
        return mCollectibleRecords.unmodifiable();
    }
    // endregion Getter ----------------------------------------------------------------------------
}
