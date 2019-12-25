package com.example.application.Collectible.dto.collAccountDTO;

import com.example.application.Shared.dto.accountRecordDTO.AccountRecordDTO;
import com.example.application.Shared.dto.accountDTO.AccountDTO;
import com.example.application.Trip.dto.tripMemberDTO.TripMemberDTO;

import java.util.List;

public class CollAccountDTO extends AccountDTO {

    // region RecordDTO ----------------------------------------------------------------------------
    public static class Record extends AccountRecordDTO<SourceType> {
        public Record(
                long date,
                String note,
                String recordAmount,
                String sourceTransId,
                SourceType sourceType,
                Effect effect) {
            super(date,
                    note,
                    recordAmount,
                    sourceTransId,
                    sourceType,
                    effect);
        }
    }
    // endregion RecordDTO  ------------------------------------------------------------------------

    // region SourceTypeDTO ------------------------------------------------------------------------
    public enum SourceType {
        BORROWING,
        LENDING,
        SETTLEMENT_MADE,
        SETTLEMENT_ACCEPTED,
        CONTRIBUTION_MADE,
        CONTRIBUTION_ACCEPTED;
    }
    // endregion SourceTypeDTO  --------------------------------------------------------------------

    public final String mTotalBalance;
    public final List<MemberDetail> mMemberDetails;

    public CollAccountDTO(String tripId, String mTotalBalance, List<MemberDetail> memberDetails) {
        super(tripId);
        this.mTotalBalance = mTotalBalance;
        mMemberDetails = memberDetails;
    }

    // region MemberDetailDTO ----------------------------------------------------------------------
    public static class MemberDetail {
        public final TripMemberDTO mTripMemberDTO;
        public final String mMemberBalance;
        public final List<Record> mAccRecords;

        public MemberDetail(TripMemberDTO tripMemberDTO, String memberBalance, List<Record> accRecords) {
            mTripMemberDTO = tripMemberDTO;
            mMemberBalance = memberBalance;
            mAccRecords = accRecords;
        }
    }
    // endregion MemberDetailDTO  ------------------------------------------------------------------


}
