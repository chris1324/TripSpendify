package com.example.application.Trip.usecase.tripExpense;

import com.example.application.Shared.baseUseCase.useCase.UseCase;
import com.example.application.Trip.repository.TripBookRepository;
import com.example.application.Shared.baseUseCase.commandUseCase.CommandUseCase;
import com.example.domain.Shared.errorhanding.outcome.Outcome;
import com.example.domain.Shared.valueObject.id.ID;
import com.example.domain.Trip.tripBook.TripBook;

public class RemoveTripExpense extends CommandUseCase<RemoveTripExpense.RequestDTO, UseCase.Void> {
    // region Variables and Constructor ------------------------------------------------------------
    private final TripBookRepository mTripBookRepository;

    public RemoveTripExpense(TripBookRepository tripBookRepository) {
        mTripBookRepository = tripBookRepository;
    }
    // endregion Variables and Constructor ---------------------------------------------------------

    // region RequestDTO  ------------------------------------------------------------------------
    // endregion RequestDTO  ---------------------------------------------------------------------
    public static class RequestDTO implements UseCase.RequestDTO {
        final String tripBookId;
        final String tripExpenseId;

        public RequestDTO(String tripBookId, String tripExpenseId) {
            this.tripBookId = tripBookId;
            this.tripExpenseId = tripExpenseId;
        }
    }

    @Override
    protected Outcome<Void> execute(RequestDTO requestDTO) {
        TripBook tripbook = mTripBookRepository.getById(ID.existingId(requestDTO.tripBookId));
        tripbook.removeExpense(ID.existingId(requestDTO.tripExpenseId));
        return Outcome.ok();
    }

    // region helper method ------------------------------------------------------------------------
    // endregion helper method ---------------------------------------------------------------------

}
