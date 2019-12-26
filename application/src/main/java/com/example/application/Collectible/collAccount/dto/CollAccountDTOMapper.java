package com.example.application.Collectible.collAccount.dto;

import com.example.application.Shared.dto.accountRecordDTO.AccountRecordDTO;
import com.example.application.Shared.dtoMapper.AccountMapper;
import com.example.application.Trip.tripMember.dto.TripMemberDTOMapper;
import com.example.domain.Collectible.collectibleRecord.CollectibleRecord;
import com.example.domain.Shared.commandBaseClass.record.BookRecord;
import com.example.domain.Shared.errorhanding.exception.UnexpectedEnumValue;
import com.example.domain.Collectible.collectibleAccount.CollectibleAccount;
import com.example.domain.Collectible.collectibleAccountDetail.CollectibleMemberDetail;

import java.util.List;
import java.util.stream.Collectors;

public class CollAccountDTOMapper implements AccountMapper<CollectibleAccount, CollAccountDTO> {

    private final TripMemberDTOMapper mTripMemberMapper;

    public CollAccountDTOMapper(TripMemberDTOMapper tripMemberMapper) {
        mTripMemberMapper = tripMemberMapper;
    }

    @Override
    public CollAccountDTO mapToDto(CollectibleAccount collectibleAccount) {
        return new CollAccountDTO(
                collectibleAccount.tripId.toString(),
                collectibleAccount.mTotalBalance.toString(),
                this.mapMemberDetail(collectibleAccount.mMemberDetails));
    }

    // region helper method ------------------------------------------------------------------------
    private List<CollAccountDTO.MemberDetail> mapMemberDetail(List<CollectibleMemberDetail> memberDetails) {
        return memberDetails.stream()
                .map(memberDetail -> new CollAccountDTO.MemberDetail(
                        mTripMemberMapper.mapToDto(memberDetail.mTripMember),
                        memberDetail.mMemberBalance.toString(),
                        mapAccountRecord(memberDetail.mAccRecord)))
                .collect(Collectors.toList());
    }

    private List<CollAccountDTO.Record> mapAccountRecord(List<CollectibleAccount.Record> records) {
        return records.stream()
                .map(record -> new CollAccountDTO.Record(
                        record.mDate.getMillisecond(),
                        record.mNote.toString(),
                        record.mAmount.toString(),
                        record.mSourceTransId.toString(),
                        this.mapSourceType(record.mSource),
                        this.mapEffect(record.mSource)))
                .collect(Collectors.toList());
    }

    private AccountRecordDTO.Effect mapEffect(CollectibleRecord.SourceType source) {
        if (source.effectOnBalance() == BookRecord.Source.Effect.INCREASE)
            return AccountRecordDTO.Effect.INCREASE;
        else return AccountRecordDTO.Effect.DECREASE;
    }

    private CollAccountDTO.SourceType mapSourceType(CollectibleRecord.SourceType source) {
        switch (source) {
            case TRIP_EXPENSE_BORROWING:
                return CollAccountDTO.SourceType.BORROWING;
            case TRIP_EXPENSE_LENDING:
                return CollAccountDTO.SourceType.LENDING;
            case COLL_SETTLEMENT_MADE:
                return CollAccountDTO.SourceType.SETTLEMENT_MADE;
            case COLL_SETTLEMENT_ACCEPTED:
                return CollAccountDTO.SourceType.SETTLEMENT_ACCEPTED;
            case COLL_CONTRIBUTION_MADE:
                return CollAccountDTO.SourceType.CONTRIBUTION_MADE;
            case COLL_CONTRIBUTION_ACCEPTED:
                return CollAccountDTO.SourceType.CONTRIBUTION_ACCEPTED;
            default:
                throw new UnexpectedEnumValue();
        }
    }
    // endregion helper method ---------------------------------------------------------------------
}
