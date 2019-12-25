package com.example.application.Trip.usecase.tripExpense;

import com.example.application.Shared.baseUseCase.queryUseCase.QueryUseCase;
import com.example.application.Shared.baseUseCase.useCase.UseCase;
import com.example.application.Trip.dto.tripExpenseDTO.TripExpenseDTO;
import com.example.application.Trip.dto.tripExpenseDTO.TripExpenseDTOMapper;
import com.example.application.Trip.repository.TripBookRepository;
import com.example.domain.Shared.errorhanding.exception.IntentionallyUnhandledError;
import com.example.domain.Shared.valueObject.id.ID;

import io.reactivex.Observable;

public class FetchTripExpense extends QueryUseCase<FetchTripExpense.RequestDTO, TripExpenseDTO> {

    // region Variables and Constructor ------------------------------------------------------------
    private final TripBookRepository mTripBookRepository;
    private final TripExpenseDTOMapper mExpenseDTOMapper;

    public FetchTripExpense(TripBookRepository tripBookRepository, TripExpenseDTOMapper expenseDTOMapper) {
        mTripBookRepository = tripBookRepository;
        mExpenseDTOMapper = expenseDTOMapper;
    }

    // endregion Variables and Constructor ---------------------------------------------------------

    // region RequestDTO  ---------------------------------------------------------------------------
    public static class RequestDTO implements UseCase.RequestDTO {
        public final String mTripBookId;
        public final String mTripExpenseId;

        public RequestDTO(String tripBookId, String tripExpenseId) {
            mTripBookId = tripBookId;
            mTripExpenseId = tripExpenseId;
        }
    }

    // endregion RequestDTO  -----------------------------------------------------------------------

    @Override
    protected Observable<TripExpenseDTO> execute(RequestDTO requestModel) {
        final ID tripBookId = ID.existingId(requestModel.mTripBookId);
        final ID tripExpenseId = ID.existingId(requestModel.mTripExpenseId);

        return mTripBookRepository.fetchById(tripBookId)
                .map(tripBook -> tripBook.getTripExpenses()
                        .get(tripExpenseId)
                        .orElseThrow(() -> IntentionallyUnhandledError.maybe("Trip ID or Expense ID incorrect")))
                .map(mExpenseDTOMapper::mapToDto);
    }
}
