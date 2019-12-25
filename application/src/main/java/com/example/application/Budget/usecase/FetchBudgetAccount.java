package com.example.application.Budget.usecase;

import com.example.application.Budget.dto.budgetAccountDTO.BudgetAccountDTO;
import com.example.application.Shared.baseUseCase.queryUseCase.QueryUseCase;
import com.example.application.Budget.repository.BudgetAccountRepository;
import com.example.application.Shared.baseUseCase.useCase.UseCase;
import com.example.application.Budget.dto.budgetAccountDTO.BudgetAccountDTOMapper;
import com.example.domain.Shared.valueObject.id.ID;

import io.reactivex.Observable;

public class FetchBudgetAccount extends QueryUseCase<FetchBudgetAccount.RequestDTO, BudgetAccountDTO> {

    // region Variables and Constructor ------------------------------------------------------------
    private final BudgetAccountRepository mAccountRepository;
    private final BudgetAccountDTOMapper mMapper;

    public FetchBudgetAccount(BudgetAccountRepository accountRepository,
                              BudgetAccountDTOMapper mapper) {
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
    protected Observable<BudgetAccountDTO> execute(RequestDTO requestModel) {
        return mAccountRepository
                .fetchByTripId(ID.existingId(requestModel.tripBookId))
                .map(mMapper::mapToDto);
    }

    // ---------------------------------------------------------------------------------------------

}
