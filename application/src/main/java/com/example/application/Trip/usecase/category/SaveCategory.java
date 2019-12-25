package com.example.application.Trip.usecase.category;

import com.example.application.Shared.baseUseCase.useCase.UseCase;
import com.example.application.Trip.repository.TripBookRepository;
import com.example.application.Shared.baseUseCase.commandUseCase.CommandUseCase;
import com.example.domain.Shared.errorhanding.exception.ImpossibleState;
import com.example.domain.Shared.errorhanding.exception.UnhandledError;
import com.example.domain.Shared.errorhanding.outcome.Outcome;
import com.example.domain.Shared.errorhanding.result.Result;
import com.example.domain.Shared.valueObject.id.ID;
import com.example.domain.Shared.valueObject.name.Name;
import com.example.domain.Shared.valueObject.uri.Uri;
import com.example.domain.Trip.category.Category;
import com.example.domain.Trip.tripBook.TripBook;

import javax.annotation.Nullable;

public class SaveCategory extends CommandUseCase<SaveCategory.RequestDTO, SaveCategory.Err> {

    // region Variables and Constructor ------------------------------------------------------------
    private final TripBookRepository mTripBookRepository;

    public SaveCategory(TripBookRepository tripBookRepository) {
        mTripBookRepository = tripBookRepository;
    }

    // endregion Variables and Constructor ---------------------------------------------------------

    public static class RequestDTO implements UseCase.RequestDTO {
        final String categoryId;
        final String tripId;
        final String name;
        final String iconUri;

        public RequestDTO(@Nullable String categoryId, String tripId, String name, String iconUri) {
            this.categoryId = categoryId;
            this.tripId = tripId;
            this.name = name;
            this.iconUri = iconUri;
        }
    }

    @Override
    protected Outcome<Err> execute(RequestDTO requestDTO) {
        // Attributes
        // -- create
        ID categoryID = this.getCategoryId(requestDTO);
        Result<Name, Name.Err.Create> name = Name.create(requestDTO.name);
        Result<Uri, Uri.Err.Create> iconUri = Uri.create(requestDTO.iconUri);

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
        TripBook tripBook = mTripBookRepository.getById(ID.existingId(requestDTO.tripId));

        // -- check if exist
        if (tripBook == null) return Outcome.err(Err.TRIP_ID_INVALID);

        // Success
        tripBook.saveCategory(category
                .getValue()
                .orElseThrow(ImpossibleState::new));
        return Outcome.ok();
    }

    // region helper method ------------------------------------------------------------------------
    private ID getCategoryId(RequestDTO requestDTO) {
        if (requestDTO.categoryId == null) return ID.newId();
        return ID.existingId(requestDTO.categoryId);
    }
    // endregion helper method ---------------------------------------------------------------------

    // region Error handing ------------------------------------------------------------------------
    public enum Err implements UseCase.ResponseDTO{
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
