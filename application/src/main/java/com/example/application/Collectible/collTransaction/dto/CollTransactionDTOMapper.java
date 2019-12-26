package com.example.application.Collectible.collTransaction.dto;

import com.example.application.Shared.dtoMapper.DtoMapper;
import com.example.domain.Collectible.collectibleTransaction.CollectibleTransaction;
import com.example.domain.Shared.errorhanding.exception.UnexpectedEnumValue;

public class CollTransactionDTOMapper implements DtoMapper<CollectibleTransaction, CollTransactionDTO> {

    @Override
    public CollTransactionDTO mapToDto(CollectibleTransaction collTrans) {
        return new CollTransactionDTO(
                collTrans.getId().toString(),
                collTrans.getDate().getMillisecond(),
                collTrans.getNote().toString(),
                collTrans.getAmount().toString(),
                this.mapTransType(collTrans),
                collTrans.getInvolvedMemberId().toString(),
                this.mapPayer(collTrans)
        );
    }

    private CollTransactionDTO.TransType mapTransType(CollectibleTransaction collTrans) {
        switch (collTrans.getTransactionType()) {
            case CONTRIBUTION:
                return CollTransactionDTO.TransType.CONTRIBUTION;
            case SETTLEMENT:
                return CollTransactionDTO.TransType.SETTLEMENT;
            default:
                throw new UnexpectedEnumValue();
        }
    }

    private CollTransactionDTO.Payer mapPayer(CollectibleTransaction collTrans) {
       if (collTrans.isUserPayer()) return CollTransactionDTO.Payer.USER;
       else return CollTransactionDTO.Payer.MEMBER;
    }
}
