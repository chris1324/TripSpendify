package com.example.application.Cash.cashAccount.usecase;

import com.example.application.Cash.cashAccount.dto.CashAccountDTO;
import com.example.application.Cash.cashAccount.dto.CashAccountDTOMapper;
import com.example.application.Cash.cashAccount.repository.CashAccountRepository;
import com.example.application.Shared.baseUseCase.queryUseCase.QueryUseCase;
import com.example.application.Shared.baseUseCase.useCase.UseCase;
import com.example.domain.Shared.valueObject.id.ID;

import io.reactivex.Observable;

public class FetchCashAccount extends QueryUseCase<FetchCashAccount.RequestDTO, CashAccountDTO> {

    // region Variables and Constructor ------------------------------------------------------------
    private final CashAccountRepository mCashAccountRepository;
    private final CashAccountDTOMapper mMapper;

    public FetchCashAccount(CashAccountRepository cashAccountRepository, CashAccountDTOMapper mapper) {
        mCashAccountRepository = cashAccountRepository;
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
    protected Observable<CashAccountDTO> execute(RequestDTO requestModel) {
        return mCashAccountRepository
                .fetchByTripId(ID.existingId(requestModel.tripBookId))
                .map(mMapper::mapToDto);
    }

    // ---------------------------------------------------------------------------------------------

}

