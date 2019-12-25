package com.example.application.Trip.usecase.tripExpense;

import com.example.application.Shared.baseUseCase.queryUseCase.QueryUseCase;
import com.example.application.Shared.baseUseCase.useCase.UseCase;
import com.example.application.Trip.dto.tripExpenseDTO.TripExpenseDTO;
import com.example.application.Trip.dto.tripExpenseDTO.TripExpenseDTOMapper;
import com.example.application.Trip.repository.TripBookRepository;
import com.example.domain.Shared.valueObject.id.ID;
import com.example.domain.Trip.tripBook.TripBook;

import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.Observable;

public class FetchAllTripExpense extends QueryUseCase<FetchAllTripExpense.RequestDTO, FetchAllTripExpense.ResponseDTO> {

    // region Variables and Constructor ------------------------------------------------------------
    private final TripBookRepository mTripBookRepository;
    private final TripExpenseDTOMapper mExpenseDTOMapper;

    public FetchAllTripExpense(TripBookRepository tripBookRepository, TripExpenseDTOMapper expenseDTOMapper) {
        mTripBookRepository = tripBookRepository;
        mExpenseDTOMapper = expenseDTOMapper;
    }

    // endregion Variables and Constructor ---------------------------------------------------------

    // region RequestDTO  --------------------------------------------------------------------------
    public static class RequestDTO implements UseCase.RequestDTO {
        public final String mTripBookId;

        public RequestDTO(String tripBookId) {
            mTripBookId = tripBookId;
        }
    }

    // endregion RequestDTO  -----------------------------------------------------------------------

    // region ResponseDTO  -------------------------------------------------------------------------
    public static class ResponseDTO implements UseCase.ResponseDTO{
        public final List<TripExpenseDTO > mTripExpensesDTO;
        public ResponseDTO(List<TripExpenseDTO> tripExpensesDTO) {
            mTripExpensesDTO = tripExpensesDTO;
        }
    }
    // endregion ResponseDTO  ----------------------------------------------------------------------

    @Override
    protected Observable<FetchAllTripExpense.ResponseDTO> execute(FetchAllTripExpense.RequestDTO requestModel) {
        return mTripBookRepository.fetchById(ID.existingId(requestModel.mTripBookId))
                .map(this::mapTripBookToTripExpensesDTO)
                .map(ResponseDTO::new);
    }

    // region helper method ------------------------------------------------------------------------
    private List<TripExpenseDTO> mapTripBookToTripExpensesDTO(TripBook tripBook){
        return tripBook
                .getTripExpenses()
                .getAll()
                .stream()
                .map(mExpenseDTOMapper::mapToDto)
                .collect(Collectors.toList());
    }
    // endregion helper method ---------------------------------------------------------------------
}
