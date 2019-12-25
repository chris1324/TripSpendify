package com.example.application.Trip.usecase.tripBook;

import com.example.application.Shared.baseUseCase.queryUseCase.QueryUseCase;
import com.example.application.Shared.baseUseCase.useCase.UseCase;
import com.example.application.Trip.dto.tripDTO.TripDTO;
import com.example.application.Trip.dto.tripDTO.TripDTOMapper;
import com.example.application.Trip.repository.TripBookRepository;

import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.Observable;

public class FetchAllTrip extends QueryUseCase<UseCase.Void, FetchAllTrip.ResponseDTO> {

    // region Variables and Constructor ------------------------------------------------------------
    private final TripBookRepository mRepository;
    private final TripDTOMapper mTripMapper;

    public FetchAllTrip(TripBookRepository repository, TripDTOMapper tripMapper) {
        mRepository = repository;
        mTripMapper = tripMapper;
    }
    // endregion Variables and Constructor ---------------------------------------------------------

    // region ResponseDTO  -------------------------------------------------------------------------
    public static class ResponseDTO implements UseCase.ResponseDTO {
        public final List<TripDTO> allTrips;

        public ResponseDTO(List<TripDTO> allTrips) {
            this.allTrips = allTrips;
        }
    }

    // endregion ResponseDTO  ----------------------------------------------------------------------

    @Override
    protected Observable<ResponseDTO> execute(Void requestModel) {
        return mRepository.fetchAll().map(tripBooks -> new ResponseDTO(tripBooks
                .stream()
                .map(mTripMapper::mapToDto)
                .collect(Collectors.toList())));
    }

    // ---------------------------------------------------------------------------------------------
}
