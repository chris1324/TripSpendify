package com.example.data.Trip.tripbook;

import com.example.data.Shared.mapper.SchemaMapper;
import com.example.data.Shared.schema.Schema;
import com.example.data.Shared.valueObjectMapper.ValueObjectMapper;
import com.example.domain.Shared.entityList.EntityList;
import com.example.domain.Shared.errorhanding.exception.ImpossibleState;
import com.example.domain.Trip.category.Category;
import com.example.domain.Trip.tripBook.TripBook;
import com.example.domain.Trip.tripExpense.TripExpense;
import com.example.domain.Trip.tripMember.TripMember;

import java.util.List;

public class TripBookSchemaMapper extends SchemaMapper<TripBook, TripBookSchemaMapper.Schema> {

    // region Variables and Constructor ------------------------------------------------------------

    public TripBookSchemaMapper(ValueObjectMapper voMapper) {
        super(voMapper);
    }

    // endregion Variables and Constructor ---------------------------------------------------------

    // region Schema -------------------------------------------------------------------------------
    public static class Schema {
        public final TripBookSchema mTripBookSchema;
        public final EntityList<Category> mCategories;
        public final EntityList<TripMember> mTripMembers;
        public final EntityList<TripExpense> mTripExpenses;

        public Schema(TripBookSchema tripBookSchema,
                      EntityList<Category> categories,
                      EntityList<TripMember> tripMembers,
                      EntityList<TripExpense> tripExpenses) {
            mTripBookSchema = tripBookSchema;
            mCategories = categories;
            mTripMembers = tripMembers;
            mTripExpenses = tripExpenses;
        }
    }
    // endregion Schema ----------------------------------------------------------------------------
    @Override
    public Schema mapToSchema(TripBook tripBook) {
        return new Schema(
                this.createTripBookSchema(tripBook),
                tripBook.getCategories(),
                tripBook.getTripMembers(),
                tripBook.getTripExpenses());
    }

    @Override
    public TripBook mapToDomain(Schema schema) {
        return TripBook
                .create(this.mapID(schema.mTripBookSchema.tripBookId),
                        schema.mCategories.getAll(),
                        schema.mTripMembers.getAll(),
                        schema.mTripExpenses.getAll(),
                        this.mapName(schema.mTripBookSchema.tripName),
                        this.mapUri(schema.mTripBookSchema.photoUri),
                        this.mapDate(schema.mTripBookSchema.startDate),
                        this.mapDate(schema.mTripBookSchema.endDate))
                .getValue()
                .orElseThrow(ImpossibleState::new);
    }

    // region helper method ------------------------------------------------------------------------
    private TripBookSchema createTripBookSchema(TripBook tripBook) {
        return new TripBookSchema(
                this.mapID(tripBook.getId()),
                this.mapName(tripBook.getTripName()),
                this.mapUri(tripBook.getPhotoUri()),
                this.mapDate(tripBook.getStartDate()),
                this.mapDate(tripBook.getEndDate()));
    }
    // endregion helper method ---------------------------------------------------------------------
}


