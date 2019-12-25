package com.example.application.Trip.usecase.tripMember;

import com.example.application.Shared.baseUseCase.useCase.UseCase;
import com.example.application.Trip.dto.tripMemberDTO.TripMemberDTO;
import com.example.application.Trip.repository.TripBookRepository;
import com.example.application.Shared.baseUseCase.commandUseCase.CommandUseCase;
import com.example.domain.Shared.errorhanding.exception.ImpossibleState;
import com.example.domain.Shared.errorhanding.exception.UnhandledError;
import com.example.domain.Shared.errorhanding.outcome.Outcome;
import com.example.domain.Shared.errorhanding.result.Result;
import com.example.domain.Shared.valueObject.contactnumber.ContactNumber;
import com.example.domain.Shared.valueObject.id.ID;
import com.example.domain.Shared.valueObject.name.Name;
import com.example.domain.Shared.valueObject.uri.Uri;
import com.example.domain.Trip.tripBook.TripBook;
import com.example.domain.Trip.tripMember.TripMember;

public class SaveMember extends CommandUseCase<SaveMember.RequestDTO, SaveMember.Err> {

    // region Variables and Constructor ------------------------------------------------------------
    private final TripBookRepository mTripBookRepository;

    public SaveMember(TripBookRepository tripBookRepository) {
        mTripBookRepository = tripBookRepository;
    }
    // endregion Variables and Constructor ---------------------------------------------------------

    // region RequestDTO  ---------------------------------------------------------------------------
    public static class RequestDTO implements UseCase.RequestDTO {
        public final String mTripBookId;
        public final TripMemberDTO mTripMemberDTO;

        public RequestDTO(String tripBookId, TripMemberDTO tripMemberDTO) {
            mTripBookId = tripBookId;
            mTripMemberDTO = tripMemberDTO;
        }
    }

    // endregion RequestDTO  -----------------------------------------------------------------------

    @Override
    protected Outcome<Err> execute(RequestDTO requestDTO) {
        // Prepare
        TripMemberDTO tripMemberDTO = requestDTO.mTripMemberDTO;

        // Attributes
        // -- create
        ID memberID = this.getMemberId(tripMemberDTO);
        Result<Name, Name.Err.Create> memberName = Name.create(tripMemberDTO.name);
        Result<Uri, Uri.Err.Create> photoUri = Uri.create(tripMemberDTO.photoUri);
        Result<ContactNumber, ContactNumber.Err.Create> contactNumber = ContactNumber.create(tripMemberDTO.contactNumber);

        // -- check and handle Err
        if (memberName.isErr()) return this.handleNameErr(memberName);
        if (photoUri.isErr()) return this.handlePhotoUriErr(photoUri);
        if (contactNumber.isErr()) return this.handleContactNumberErr(contactNumber);

        // TripMember
        // -- create
        Result<TripMember, TripMember.Err.Create> tripMember = TripMember.create(
                memberID,
                memberName.getValue().orElseThrow(ImpossibleState::new),
                photoUri.getValue().orElseThrow(ImpossibleState::new),
                contactNumber.getValue().orElseThrow(ImpossibleState::new)
        );

        // -- check and handle Err
        if (tripMember.isErr()) return this.handleMemberErr(tripMember);

        // TripBook
        // -- get from repo
        TripBook tripBook = mTripBookRepository.getById(ID.existingId(requestDTO.mTripBookId));

        // -- check if exist
        if (tripBook == null) return Outcome.err(Err.TRIP_ID_INVALID);

        // Success
        tripBook.saveMember(tripMember
                .getValue()
                .orElseThrow(ImpossibleState::new));
        return Outcome.ok();
    }

    // region helper method ------------------------------------------------------------------------
    private ID getMemberId(TripMemberDTO tripMemberDTO) {
        if (tripMemberDTO.memberId == null) return ID.newId();
        return ID.existingId(tripMemberDTO.memberId);
    }
    // endregion helper method ---------------------------------------------------------------------

    // region Error handing ------------------------------------------------------------------------
    public enum Err implements UseCase.ResponseDTO {
        TRIP_ID_INVALID,
        NAME_NULL,
        NAME_CHAR_LESS_THAN_FOUR,
        PHOTO_URI_NULL,
        PHOTO_URI_INVALID,
        CONTRACT_NUMBER_NULL,
        CONTRACT_NUMBER_INVALID
    }

    private Outcome<Err> handleMemberErr(Result<TripMember, TripMember.Err.Create> tripMember) {
        switch (tripMember.getError().orElseThrow(ImpossibleState::new)) {
            case NULL_ARGUMENT:
                throw new ImpossibleState();
            default:
                throw new UnhandledError();
        }
    }

    private Outcome<Err> handleNameErr(Result<Name, Name.Err.Create> memberName) {
        switch (memberName.getError().orElseThrow(ImpossibleState::new)) {
            case NAME_NULL:
                return Outcome.err(Err.NAME_NULL);
            case NAME_CHAR_LESS_THAN_FOUR:
                return Outcome.err(Err.NAME_CHAR_LESS_THAN_FOUR);
            default:
                throw new UnhandledError();
        }

    }

    private Outcome<Err> handlePhotoUriErr(Result<Uri, Uri.Err.Create> photoUri) {
        switch (photoUri.getError().orElseThrow(ImpossibleState::new)) {
            case URI_NULL:
                return Outcome.err(Err.PHOTO_URI_NULL);
            case URI_INVALID:
                return Outcome.err(Err.PHOTO_URI_INVALID);
            default:
                throw new UnhandledError();
        }

    }

    private Outcome<Err> handleContactNumberErr(Result<ContactNumber, ContactNumber.Err.Create> contactNumber) {
        switch (contactNumber.getError().orElseThrow(ImpossibleState::new)) {
            case CONTRACT_NUMBER_NULL:
                return Outcome.err(Err.CONTRACT_NUMBER_NULL);
            case CONTRACT_NUMBER_INVALID:
            case CONTRACT_NUMBER_EMPTY:
                return Outcome.err(Err.CONTRACT_NUMBER_INVALID);
            default:
                throw new UnhandledError();
        }
    }
    // endregion Error handing ---------------------------------------------------------------------
}
