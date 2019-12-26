package com.example.application.Trip.tripBook.usecase;

import com.example.application.Shared.baseUseCase.queryUseCase.QueryUseCase;
import com.example.application.Shared.baseUseCase.useCase.UseCase;
import com.example.application.Trip.tripBook.dto.TripBookMiniDTO;
import com.example.application.Trip.tripBook.dto.TripBookMiniDTOMapper;
import com.example.application.Trip.tripBook.repository.TripBookRepository;
import com.example.domain.Shared.valueObject.id.ID;

import io.reactivex.Observable;

public class FetchTrip extends QueryUseCase<FetchTrip.RequestDTO, TripBookMiniDTO> {

    // region Variables and Constructor ------------------------------------------------------------
    private final TripBookRepository mRepository;
    private final TripBookMiniDTOMapper mTripMapper;

    public FetchTrip(TripBookRepository repository, TripBookMiniDTOMapper tripMapper) {
        mRepository = repository;
        mTripMapper = tripMapper;
    }

    // endregion Variables and Constructor ---------------------------------------------------------

    // region RequestDTO  --------------------------------------------------------------------------
    public static class RequestDTO implements UseCase.RequestDTO {
        public final String tripId;

        public RequestDTO(String tripId) {
            this.tripId = tripId;
        }
    }
    // endregion RequestDTO  -----------------------------------------------------------------------

    @Override
    protected Observable<TripBookMiniDTO> execute(RequestDTO requestModel) {
        return mRepository
                .fetchMinimalById(ID.existingId(requestModel.tripId))
                .map(mTripMapper::mapToDto);
    }

    // region helper method ------------------------------------------------------------------------
    // endregion helper method ---------------------------------------------------------------------



}
