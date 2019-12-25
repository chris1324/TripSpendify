package com.example.application.Trip.usecase.tripBook;

import com.example.application.Shared.baseUseCase.queryUseCase.QueryUseCase;
import com.example.application.Shared.baseUseCase.useCase.UseCase;
import com.example.application.Trip.dto.tripDTO.TripDTO;
import com.example.application.Trip.dto.tripDTO.TripDTOMapper;
import com.example.application.Trip.repository.TripBookRepository;
import com.example.domain.Shared.valueObject.id.ID;

import io.reactivex.Observable;

public class FetchTrip extends QueryUseCase<FetchTrip.RequestDTO,TripDTO> {

    // region Variables and Constructor ------------------------------------------------------------
    private final TripBookRepository mRepository;
    private final TripDTOMapper mTripMapper;

    public FetchTrip(TripBookRepository repository, TripDTOMapper tripMapper) {
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
    protected Observable<TripDTO> execute(RequestDTO requestModel) {
        return mRepository
                .fetchById(ID.existingId(requestModel.tripId))
                .map(mTripMapper::mapToDto);
    }

    // region helper method ------------------------------------------------------------------------
    // endregion helper method ---------------------------------------------------------------------



}
