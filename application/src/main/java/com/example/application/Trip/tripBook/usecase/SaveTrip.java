package com.example.application.Trip.tripBook.usecase;

import com.example.application.Shared.baseUseCase.useCase.UseCase;
import com.example.application.Shared.baseUseCase.commandUseCase.CommandUseCase;
import com.example.application.Trip.tripBook.dto.TripBookMiniDTO;
import com.example.application.Trip.tripBook.repository.TripBookRepository;
import com.example.domain.Shared.errorhanding.exception.ImpossibleState;
import com.example.domain.Shared.errorhanding.exception.UnhandledError;
import com.example.domain.Shared.errorhanding.outcome.Outcome;
import com.example.domain.Shared.errorhanding.result.Result;
import com.example.domain.Shared.valueObject.date.Date;
import com.example.domain.Shared.valueObject.id.ID;
import com.example.domain.Shared.valueObject.name.Name;
import com.example.domain.Shared.valueObject.uri.Uri;
import com.example.domain.Trip.category.Category;
import com.example.domain.Trip.tripBook.TripBook;


import java.util.ArrayList;
import java.util.List;

public class SaveTrip extends CommandUseCase<TripBookMiniDTO, SaveTrip.Err> {


    // region Variables and Constructor ------------------------------------------------------------
    private final TripBookRepository mTripBkRepo;

    public SaveTrip(TripBookRepository tripBkRepo) {
        mTripBkRepo = tripBkRepo;
    }

    // endregion Variables and Constructor ---------------------------------------------------------

    @Override
    protected Outcome<Err> execute(TripBookMiniDTO tripDTO) {
        // Attributes
        // -- create
        ID tripBookId = this.getTripBookId(tripDTO);
        List<Category> categories = this.createDefaultCategories();
        Result<Name, Name.Err.Create> tripName = Name.create(tripDTO.tripName);
        Result<Uri, Uri.Err.Create> photoUri = Uri.create(tripDTO.photoUri);
        Result<Date, Date.Err.Create> startDate = Date.create(tripDTO.startDateMillis);
        Result<Date, Date.Err.Create> endDate = Date.create(tripDTO.endDateMillis);

        // -- check and handle Err
        if (tripName.isErr()) return this.handleTripNameErr(tripName);
        if (photoUri.isErr()) return this.handlePhotoUriErr(photoUri);
        if (startDate.isErr()) return this.handleStartDateErr(startDate);
        if (endDate.isErr()) return this.handleEndDateErr(endDate);

        // TripBook
        // -- create
        Result<TripBook, TripBook.Err.Create> tripBook = TripBook.create(
                tripBookId,
                categories,
                new ArrayList<>(),
                new ArrayList<>(),
                tripName.getValue().orElseThrow(ImpossibleState::new),
                photoUri.getValue().orElseThrow(ImpossibleState::new),
                startDate.getValue().orElseThrow(ImpossibleState::new),
                endDate.getValue().orElseThrow(ImpossibleState::new)
        );

        // -- check and handle Err
        if (tripBook.isErr()) return this.handleTripBookErr(tripBook);

        // Success
        mTripBkRepo.save(tripBook
                .getValue()
                .orElseThrow(ImpossibleState::new));
        return Outcome.ok();
    }


    // region helper method ------------------------------------------------------------------------
    private List<Category> createDefaultCategories() {
        // TODO Use default list of categories
        return null;
    }

    private ID getTripBookId(TripBookMiniDTO tripDTO) {
        if (tripDTO.tripBookId == null) return ID.newId();
        return ID.existingId(tripDTO.tripBookId);
    }

    // endregion helper method ---------------------------------------------------------------------

    // region Error handing ------------------------------------------------------------------------
    public enum Err implements UseCase.ResponseDTO{
        TRIP_NAME_NULL,
        TRIP_NAME_CHAR_LESS_THAN_FOUR,
        PHOTO_URI_NULL,
        PHOTO_URI_INVALID,
        START_DATE_NULL,
        END_DATE_NULL,
        START_AFTER_END_DATE
    }

    private Outcome<Err> handleTripBookErr(Result<TripBook, TripBook.Err.Create> tripBook) {
        switch (tripBook.getError().orElseThrow(ImpossibleState::new)) {
            case START_AFTER_END_DATE:
                return Outcome.err(Err.START_AFTER_END_DATE);
            case NULL_ARGUMENT:
            case EMPTY_CATEGORY:
                throw new ImpossibleState();
            default:
                throw new UnhandledError();
        }
    }

    private Outcome<Err> handleTripNameErr(Result<Name, Name.Err.Create> tripName) {
        switch (tripName.getError().orElseThrow(ImpossibleState::new)) {
            case NAME_NULL:
                return Outcome.err(Err.TRIP_NAME_NULL);
            case NAME_CHAR_LESS_THAN_FOUR:
                return Outcome.err(Err.TRIP_NAME_CHAR_LESS_THAN_FOUR);
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

    private Outcome<Err> handleStartDateErr(Result<Date, Date.Err.Create> startDate) {
        switch (startDate.getError().orElseThrow(ImpossibleState::new)) {
            case MILLISECOND_IS_ZERO:
                return Outcome.err(Err.START_DATE_NULL);
            default:
                throw new UnhandledError();
        }
    }

    private Outcome<Err> handleEndDateErr(Result<Date, Date.Err.Create> endDate) {
        switch (endDate.getError().orElseThrow(ImpossibleState::new)) {
            case MILLISECOND_IS_ZERO:
                return Outcome.err(Err.END_DATE_NULL);
            default:
                throw new UnhandledError();
        }
    }
    // endregion Error handing ---------------------------------------------------------------------


}
