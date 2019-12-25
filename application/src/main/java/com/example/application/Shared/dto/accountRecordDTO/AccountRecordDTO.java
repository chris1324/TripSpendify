package com.example.application.Shared.dto.accountRecordDTO;

import com.example.domain.Shared.commandBaseClass.record.BookRecord;

public abstract class AccountRecordDTO<SourceTypeDTO> {

    public enum Effect {
        INCREASE,
        DECREASE;

        public static <SourceType extends BookRecord.Source> Effect valueOf(SourceType sourceType){
            if (sourceType.effectOnBalance() == BookRecord.Source.Effect.INCREASE)
                return AccountRecordDTO.Effect.INCREASE;
            else return AccountRecordDTO.Effect.DECREASE;
        }
    }

    public final long mDate;
    public final String mNote;
    public final String mRecordAmount;
    public final String mSourceTransId;
    public final SourceTypeDTO mSourceTypeDTO;
    public final Effect mEffect;

    public AccountRecordDTO(long date, String note, String recordAmount, String sourceTransId, SourceTypeDTO sourceTypeDTO, Effect effect) {
        mDate = date;
        mNote = note;
        mRecordAmount = recordAmount;
        mSourceTransId = sourceTransId;
        mSourceTypeDTO = sourceTypeDTO;
        mEffect = effect;
    }
}
