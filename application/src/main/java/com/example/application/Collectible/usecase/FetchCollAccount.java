package com.example.application.Collectible.usecase;

import com.example.application.Collectible.repository.CollectibleAccountRepository;
import com.example.application.Shared.baseUseCase.queryUseCase.QueryUseCase;
import com.example.application.Shared.baseUseCase.useCase.UseCase;
import com.example.application.Collectible.dto.collAccountDTO.CollAccountDTO;
import com.example.application.Collectible.dto.collAccountDTO.CollAccountDTOMapper;
import com.example.domain.Shared.valueObject.id.ID;

import io.reactivex.Observable;

public class FetchCollAccount extends QueryUseCase<FetchCollAccount.RequestDTO, CollAccountDTO> {

    // region Variables and Constructor ------------------------------------------------------------
    private final CollectibleAccountRepository mAccountRepository;
    private final CollAccountDTOMapper mMapper;

    public FetchCollAccount(CollectibleAccountRepository accountRepository, CollAccountDTOMapper mapper) {
        mAccountRepository = accountRepository;
        mMapper = mapper;
    }

    // endregion Variables and Constructor ---------------------------------------------------------

    // region RequestDTO  --------------------------------------------------------------------------
    public static class RequestDTO implements UseCase.RequestDTO {
        final String tripBookId;

        public RequestDTO(String tripBookId) {
            this.tripBookId = tripBookId;
        }
    }
    // endregion RequestDTO  -----------------------------------------------------------------------

    @Override
    protected Observable<CollAccountDTO> execute(RequestDTO requestModel) {
        return mAccountRepository
                .fetchByTripId(ID.existingId(requestModel.tripBookId))
                .map(mMapper::mapToDto);
    }

    // ---------------------------------------------------------------------------------------------
}
