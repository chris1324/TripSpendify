package com.example.domain.Collectible.collectibleAccountDetail;

import com.example.domain.Collectible.collectibleAccount.CollectibleAccount;
import com.example.domain.Shared.queryBaseClass.account.Account;
import com.example.domain.Shared.valueObject.contactnumber.ContactNumber;
import com.example.domain.Shared.valueObject.id.ID;
import com.example.domain.Shared.valueObject.name.Name;
import com.example.domain.Shared.valueObject.numeric.Amount;
import com.example.domain.Shared.valueObject.uri.Uri;
import com.example.domain.Trip.tripMember.TripMember;

import java.util.List;

public class CollectibleMemberDetail {

    public final TripMember mTripMember;
    public final Amount mMemberBalance;
    public final List<CollectibleAccount.Record> mAccRecord;

    public CollectibleMemberDetail(TripMember tripMember, Amount memberBalance, List<CollectibleAccount.Record> accRecord) {
        mTripMember = tripMember;
        mMemberBalance = memberBalance;
        mAccRecord = accRecord;
    }
}
