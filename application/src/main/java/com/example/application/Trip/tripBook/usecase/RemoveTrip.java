package com.example.application.Trip.tripBook.usecase;

import com.example.application.Shared.baseUseCase.useCase.UseCase;
import com.example.application.Shared.baseUseCase.commandUseCase.CommandUseCase;
import com.example.application.Trip.tripBook.repository.TripBookRepository;
import com.example.domain.Shared.errorhanding.outcome.Outcome;
import com.example.domain.Shared.valueObject.id.ID;

public class RemoveTrip extends CommandUseCase<RemoveTrip.RequestDTO, UseCase.Void> {


    // region Variables and Constructor ------------------------------------------------------------
    private final TripBookRepository mTripBookRepository;

    public RemoveTrip(TripBookRepository tripBookRepository) {
        mTripBookRepository = tripBookRepository;
    }

    // endregion Variables and Constructor ---------------------------------------------------------

    // region RequestDTO  ------------------------------------------------------------------------
    public static class RequestDTO implements UseCase.RequestDTO {
        final String tripBookId;

        public RequestDTO(String tripBookId) {
            this.tripBookId = tripBookId;
        }
    }
    // endregion RequestDTO  ---------------------------------------------------------------------


    @Override
    protected Outcome<Void> execute(RequestDTO requestDTO) {
        mTripBookRepository.remove(ID.existingId(requestDTO.tripBookId));
        return Outcome.ok();
    }

    // region helper method ------------------------------------------------------------------------
    // endregion helper method ---------------------------------------------------------------------

}
