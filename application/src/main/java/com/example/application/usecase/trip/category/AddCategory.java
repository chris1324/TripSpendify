package com.example.application.usecase.trip.category;

import com.example.application.common.repository.TripBookRepository;
import com.example.application.common.usecase.CommandUseCase;
import com.example.domain.Common.errorhanding.exception.ImpossibleState;
import com.example.domain.Common.errorhanding.exception.UnhandledError;
import com.example.domain.Common.errorhanding.outcome.Outcome;
import com.example.domain.Common.errorhanding.result.Result;
import com.example.domain.Common.sharedValueObject.id.ID;
import com.example.domain.Common.sharedValueObject.name.Name;
import com.example.domain.Common.sharedValueObject.uri.Uri;
import com.example.domain.Trip.category.Category;
import com.example.domain.Trip.tripBook.TripBook;

public class AddCategory extends CommandUseCase<AddCategory.RequestModel, AddCategory.Err> {

    // region Variables and Constructor ------------------------------------------------------------
    private final TripBookRepository mTripBookRepository;

    public AddCategory(TripBookRepository tripBookRepository) {
        mTripBookRepository = tripBookRepository;
    }

    // endregion Variables and Constructor ---------------------------------------------------------

    public static class RequestModel {
        final String tripId;
        final String name;
        final String iconUri;

        public RequestModel(String tripId, String name, String iconUri) {
            this.tripId = tripId;
            this.name = name;
            this.iconUri = iconUri;
        }
    }

    @Override
    protected Outcome<Err> execute(RequestModel requestModel) {
        // Attributes
        // -- create
        Result<Name, Name.Err.Create> name = Name.create(requestModel.name);
        Result<Uri, Uri.Err.Create> iconUri = Uri.create(requestModel.iconUri);

        // -- check and handle Err
        if (name.isErr()) return this.handleNameErr(name);
        if (iconUri.isErr()) return this.handleIconUriErr(iconUri);

        // Category
        // -- create
        Result<Category, Category.Err.Create> category = Category.create(
                ID.newId(),
                name.getValue().orElseThrow(ImpossibleState::new),
                iconUri.getValue().orElseThrow(ImpossibleState::new)
        );

        // -- check and handle Err
        if (category.isErr()) return this.handleCategoryErr(category);

        // TripBook
        // -- get from repo
        TripBook tripBook = mTripBookRepository.getTripBookById(ID.existingId(requestModel.tripId));

        // -- check if exist
        if (tripBook == null) return Outcome.err(Err.TRIP_ID_INVALID);

        // Success
        tripBook.addCategory(category
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
        URI_NULL,
        URI_INVALID

    }

    private Outcome<Err> handleCategoryErr(Result<Category, Category.Err.Create> category) {
        switch (category.getError().orElseThrow(ImpossibleState::new)) {
            case NULL_ARGUMENT:
                throw new ImpossibleState();
            default:
                throw new UnhandledError();
        }
    }

    private Outcome<Err> handleNameErr(Result<Name, Name.Err.Create> name) {
        switch (name.getError().orElseThrow(ImpossibleState::new)) {
            case NAME_NULL:
                return Outcome.err(Err.NAME_NULL);
            case NAME_CHAR_LESS_THAN_FOUR:
                return Outcome.err(Err.NAME_CHAR_LESS_THAN_FOUR);
            default:
                throw new UnhandledError();
        }
    }

    private Outcome<Err> handleIconUriErr(Result<Uri, Uri.Err.Create> iconUri) {
        switch (iconUri.getError().orElseThrow(ImpossibleState::new)) {
            case URI_NULL:
                return Outcome.err(Err.URI_NULL);
            case URI_INVALID:
                return Outcome.err(Err.URI_INVALID);
            default:
                throw new UnhandledError();
        }
    }
    // endregion Error handing ---------------------------------------------------------------------
}
