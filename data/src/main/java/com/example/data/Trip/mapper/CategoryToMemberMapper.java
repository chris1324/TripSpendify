package com.example.data.Trip.mapper;

import com.example.data.Shared.mapper.BaseToSchemaMapper;
import com.example.data.Shared.mapper.SchemaMapper;
import com.example.data.Shared.valueObjectMapper.ValueObjectMapper;
import com.example.data.Trip.dao.CategoryDAO;
import com.example.data.Trip.schema.category.CategorySchema;
import com.example.domain.Shared.errorhanding.exception.ImpossibleState;
import com.example.domain.Shared.valueObject.id.ID;
import com.example.domain.Trip.category.Category;

public class CategoryToMemberMapper extends SchemaMapper<CategoryToMemberMapper.Domain, CategorySchema> {

    // region Variables and Constructor ------------------------------------------------------------
    private final ValueObjectMapper mVoMapper;

    public CategoryToMemberMapper(ValueObjectMapper voMapper) {
        mVoMapper = voMapper;
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
                mVoMapper.mapID(domain.mCategory.getId()),
                mVoMapper.mapID(domain.mTripBookId),
                mVoMapper.mapName(domain.mCategory.getCategoryName()),
                mVoMapper.mapUri(domain.mCategory.getIconUri()));
    }

    @Override
    public Domain mapToDomain(CategorySchema categorySchema) {
        return new Domain(
                mVoMapper.mapID(categorySchema.tripBookId),
                Category
                        .create(
                                mVoMapper.mapID(categorySchema.categoryId),
                                mVoMapper.mapName(categorySchema.name),
                                mVoMapper.mapUri(categorySchema.iconUri)
                        ).getValue().orElseThrow(ImpossibleState::new)
        );
    }

    // ---------------------------------------------------------------------------------------------
}
