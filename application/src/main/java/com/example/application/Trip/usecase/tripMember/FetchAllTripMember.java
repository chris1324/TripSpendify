package com.example.application.Trip.usecase.tripMember;

import com.example.application.Shared.baseUseCase.queryUseCase.QueryUseCase;
import com.example.application.Shared.baseUseCase.useCase.UseCase;
import com.example.application.Trip.dto.tripMemberDTO.TripMemberDTO;
import com.example.application.Trip.dto.tripMemberDTO.TripMemberDTOMapper;
import com.example.application.Trip.repository.TripBookRepository;
import com.example.domain.Shared.valueObject.id.ID;
import com.example.domain.Trip.tripBook.TripBook;

import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.Observable;

public class FetchAllTripMember extends QueryUseCase<RemoveMember.RequestDTO, FetchAllTripMember.ResponseDTO> {

    // region Variables and Constructor ------------------------------------------------------------
    private final TripBookRepository mRepository;
    private final TripMemberDTOMapper mMapper;

    public FetchAllTripMember(TripBookRepository repository, TripMemberDTOMapper mapper) {
        mRepository = repository;
        mMapper = mapper;
    }

    // endregion Variables and Constructor ---------------------------------------------------------

    // region RequestDTO  --------------------------------------------------------------------------
    public static class RequestDTO implements UseCase.RequestDTO {
        public final String tripBookId;

        public RequestDTO(String tripBookId) {
            this.tripBookId = tripBookId;
        }
    }

    // endregion RequestDTO  -----------------------------------------------------------------------

    // region ResponseDTO  -------------------------------------------------------------------------
    public static class ResponseDTO implements UseCase.ResponseDTO {
        public final List<TripMemberDTO> allTripMember;

        public ResponseDTO(List<TripMemberDTO> allTripMember) {
            this.allTripMember = allTripMember;
        }
    }

    // endregion ResponseDTO  ----------------------------------------------------------------------

    @Override
    protected Observable<FetchAllTripMember.ResponseDTO> execute(RemoveMember.RequestDTO requestModel) {
        return mRepository
                .fetchById(ID.existingId(requestModel.tripBookId))
                .map(this::mapTripBookToTripMembersDTO)
                .map(ResponseDTO::new);
    }

    // region helper method ------------------------------------------------------------------------
    private List<TripMemberDTO> mapTripBookToTripMembersDTO(TripBook tripBook) {
        return tripBook
                .getTripMembers()
                .getAll()
                .stream()
                .map(mMapper::mapToDto)
                .collect(Collectors.toList());
    }
    // endregion helper method ---------------------------------------------------------------------
}
