package com.example.application.Trip.tripBook.usecase;

import com.example.application.Shared.baseUseCase.queryUseCase.QueryUseCase;
import com.example.application.Shared.baseUseCase.useCase.UseCase;
import com.example.application.Trip.tripBook.dto.TripBookMiniDTO;
import com.example.application.Trip.tripBook.dto.TripBookMiniDTOMapper;
import com.example.application.Trip.tripBook.repository.TripBookRepository;

import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.Observable;

public class FetchAllTrip extends QueryUseCase<UseCase.Void, FetchAllTrip.ResponseDTO> {

    // region Variables and Constructor ------------------------------------------------------------
    private final TripBookRepository mRepository;
    private final TripBookMiniDTOMapper mTripMapper;

    public FetchAllTrip(TripBookRepository repository, TripBookMiniDTOMapper tripMapper) {
        mRepository = repository;
        mTripMapper = tripMapper;
    }
    // endregion Variables and Constructor ---------------------------------------------------------

    // region ResponseDTO  -------------------------------------------------------------------------
    public static class ResponseDTO implements UseCase.ResponseDTO {
        public final List<TripBookMiniDTO> allTrips;

        public ResponseDTO(List<TripBookMiniDTO> allTrips) {
            this.allTrips = allTrips;
        }
    }

    // endregion ResponseDTO  ----------------------------------------------------------------------

    @Override
    protected Observable<ResponseDTO> execute(Void requestModel) {
        return mRepository.fetchAllMinimal().map(tripBooks -> new ResponseDTO(tripBooks
                .stream()
                .map(mTripMapper::mapToDto)
                .collect(Collectors.toList())));
    }

    // ---------------------------------------------------------------------------------------------
}
