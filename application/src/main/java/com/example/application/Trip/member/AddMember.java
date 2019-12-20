package com.example.application.Trip.member;

import com.example.application.Common.repository.TripBookRepository;
import com.example.application.Common.usecase.CommandUseCase;
import com.example.domain.Common.errorhanding.exception.ImpossibleState;
import com.example.domain.Common.errorhanding.exception.UnhandledError;
import com.example.domain.Common.errorhanding.outcome.Outcome;
import com.example.domain.Common.errorhanding.result.Result;
import com.example.domain.Common.sharedvalueobject.contactnumber.ContactNumber;
import com.example.domain.Common.sharedvalueobject.id.ID;
import com.example.domain.Common.sharedvalueobject.name.Name;
import com.example.domain.Common.sharedvalueobject.uri.Uri;
import com.example.domain.Trip.tripbook.TripBook;
import com.example.domain.Trip.tripmember.TripMember;

public class AddMember extends CommandUseCase<AddMember.RequestModel, AddMember.Err> {

    // region Variables and Constructor ------------------------------------------------------------
    private final TripBookRepository mTripBookRepository;

    public AddMember(TripBookRepository tripBookRepository) {
        mTripBookRepository = tripBookRepository;
    }
    // endregion Variables and Constructor ---------------------------------------------------------

    public static class RequestModel {
        final String tripId;
        final String name;
        final String photoUri;
        final String contactNumber;

        public RequestModel(String tripId, String name, String photoUri, String contactNumber) {
            this.tripId = tripId;
            this.name = name;
            this.photoUri = photoUri;
            this.contactNumber = contactNumber;
        }
    }

    @Override
    protected Outcome<Err> execute(RequestModel requestModel) {
        // Attributes
        // -- create
        Result<Name, Name.Err.Create> memberName = Name.create(requestModel.name);
        Result<Uri, Uri.Err.Create> photoUri = Uri.create(requestModel.photoUri);
        Result<ContactNumber, ContactNumber.Err.Create> contactNumber = ContactNumber.create(requestModel.contactNumber);

        // -- check and handle Err
        if (memberName.isErr()) return this.handleNameErr(memberName);
        if (photoUri.isErr()) return this.handlePhotoUriErr(photoUri);
        if (contactNumber.isErr()) return this.handleContactNumberErr(contactNumber);

        // TripMember
        // -- create
        Result<TripMember, TripMember.Err.Create> tripMember = TripMember.create(
                ID.newId(),
                memberName.getValue().orElseThrow(ImpossibleState::new),
                photoUri.getValue().orElseThrow(ImpossibleState::new),
                contactNumber.getValue().orElseThrow(ImpossibleState::new)
        );

        // -- check and handle Err
        if (tripMember.isErr()) return this.handleMemberErr(tripMember);

        // TripBook
        // -- get from repo
        TripBook tripBook = mTripBookRepository.getTripBookById(ID.existingId(requestModel.tripId));

        // -- check if exist
        if (tripBook == null) return Outcome.err(Err.TRIP_ID_INVALID);

        // Success
        tripBook.addMember(tripMember
                .getValue()
                .orElseThrow(ImpossibleState::new));
        return Outcome.ok();
    }


    // region helper method ------------------------------------------------------------------------
    // endregion helper method ---------------------------------------------------------------------

    // region Error handing ------------------------------------------------------------------------
    public enum Err {
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
