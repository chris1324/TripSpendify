package com.example.domain.Collectible.collectibleAccount;

import com.example.domain.Collectible.collectibleAccountDetail.CollectibleMemberDetail;
import com.example.domain.Collectible.collectibleRecord.CollectibleRecord;
import com.example.domain.Shared.queryBaseClass.account.Account;
import com.example.domain.Shared.queryBaseClass.acountRecord.AccountRecord;
import com.example.domain.Shared.valueObject.date.Date;
import com.example.domain.Shared.valueObject.id.ID;
import com.example.domain.Shared.valueObject.note.Note;
import com.example.domain.Shared.valueObject.numeric.Amount;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;


public class CollectibleAccount extends Account {

    // region Record -------------------------------------------------------------------------------
    public class Record extends AccountRecord<CollectibleRecord.SourceType> {
        public Record(ID sourceTransId, CollectibleRecord.SourceType source, Amount amount, Date date, Note note) {
            super(sourceTransId, source, amount, date, note);
        }
    }
    // endregion Record  ---------------------------------------------------------------------------

    public final Amount mTotalBalance;
    public final List<CollectibleMemberDetail> mMemberDetails;

    public CollectibleAccount(ID tripId, List<CollectibleMemberDetail> memberDetails) {
        super(tripId);
        mMemberDetails = memberDetails;
        mTotalBalance = this.calculateBalance(
                memberDetails,
                memberDetail -> memberDetail.mMemberBalance);
    }

    // region helper method ------------------------------------------------------------------------
    private Amount calculateBalance(List<CollectibleMemberDetail> memberDetails, Function<CollectibleMemberDetail, Amount> mapFunction) {
        final List<Amount> memberBalances = memberDetails.stream()
                .map(mapFunction)
                .collect(Collectors.toList());
        return Amount.calculator().sum(memberBalances);
    }
    // endregion helper method ---------------------------------------------------------------------
}
