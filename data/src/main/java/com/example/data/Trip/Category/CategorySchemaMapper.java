package com.example.data.Trip.Category;

import com.example.data.Shared.mapper.SchemaMapper;
import com.example.data.Shared.valueObjectMapper.ValueObjectMapper;
import com.example.domain.Shared.errorhanding.exception.ImpossibleState;
import com.example.domain.Shared.valueObject.id.ID;
import com.example.domain.Trip.category.Category;

public class CategorySchemaMapper extends SchemaMapper<CategorySchemaMapper.Domain, CategorySchema> {


    // region Variables and Constructor ------------------------------------------------------------

    public CategorySchemaMapper(ValueObjectMapper voMapper) {
        super(voMapper);
    }

    // endregion Variables and Constructor ---------------------------------------------------------

    // region Domain -------------------------------------------------------------------------------
    public static class Domain {
        public final ID mTripBookId;
        public final Category mCategory;

        public Domain(ID tripBookId, Category category) {
            mTripBookId = tripBookId;
            mCategory = category;
        }
    }
    // endregion Domain ----------------------------------------------------------------------------

    @Override
    public CategorySchema mapToSchema(Domain domain) {
        return new CategorySchema(
                this.mapID(domain.mCategory.getId()),
                this.mapID(domain.mTripBookId),
                this.mapName(domain.mCategory.getCategoryName()),
                this.mapUri(domain.mCategory.getIconUri()));
    }

    @Override
    public Domain mapToDomain(CategorySchema categorySchema) {
        return new Domain(
                this.mapID(categorySchema.tripBookId),
                Category
                        .create(
                                this.mapID(categorySchema.categoryId),
                                this.mapName(categorySchema.name),
                                this.mapUri(categorySchema.iconUri))
                        .getValue()
                        .orElseThrow(ImpossibleState::new));
    }

    // ---------------------------------------------------------------------------------------------
}
