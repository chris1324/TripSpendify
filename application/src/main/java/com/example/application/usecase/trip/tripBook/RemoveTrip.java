package com.example.application.usecase.trip.tripBook;

import com.example.application.common.repository.TripBookRepository;
import com.example.application.common.usecase.CommandUseCase;
import com.example.domain.Common.domaineEventBus.DomainEventBus;
import com.example.domain.Common.errorhanding.outcome.Outcome;
import com.example.domain.Common.sharedValueObject.id.ID;
import com.example.domain.Trip.tripBook.TripBook;

public class RemoveTrip  extends CommandUseCase<RemoveTrip.RequestModel, RemoveTrip.Err> {


    // region Variables and Constructor ------------------------------------------------------------
    private final TripBookRepository mTripBookRepository;

    public RemoveTrip(TripBookRepository tripBookRepository) {
        mTripBookRepository = tripBookRepository;
    }

    // endregion Variables and Constructor ---------------------------------------------------------

    // region RequestModel  ------------------------------------------------------------------------
    public static class RequestModel {
        final String tripId;

        public RequestModel(String tripId) {
            this.tripId = tripId;
        }
    }
    // endregion RequestModel  ---------------------------------------------------------------------


    @Override
    protected Outcome<Err> execute(RequestModel requestModel) {
        TripBook tripBook = mTripBookRepository.getTripBookById(ID.existingId(requestModel.tripId));
        mTripBookRepository.remove(tripBook.getId());
        return Outcome.ok();
    }

    // region helper method ------------------------------------------------------------------------
    // endregion helper method ---------------------------------------------------------------------

    // region Error handing ------------------------------------------------------------------------
    public enum Err {}
    // endregion Error handing ---------------------------------------------------------------------
}
