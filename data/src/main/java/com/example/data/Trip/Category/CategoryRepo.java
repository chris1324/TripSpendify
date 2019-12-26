package com.example.data.Trip.Category;

import com.example.data.Shared.repository.BaseRepository;
import com.example.data.Shared.valueObjectMapper.ValueObjectMapper;
import com.example.domain.Shared.entityList.EntityList;
import com.example.domain.Shared.valueObject.id.ID;
import com.example.domain.Trip.category.Category;

import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.Observable;

public class CategoryRepo extends BaseRepository {

    // region Variables and Constructor ------------------------------------------------------------
    private final CategoryDao mCategoryDao;
    private final CategorySchemaMapper mSchemaMapper;

    public CategoryRepo(ValueObjectMapper voMapper, CategoryDao categoryDao, CategorySchemaMapper schemaMapper) {
        super(voMapper);
        mCategoryDao = categoryDao;
        mSchemaMapper = schemaMapper;
    }

    // endregion Variables and Constructor ---------------------------------------------------------

    // ---------------------------------------- Command --------------------------------------------
    public void save(ID tripBookId, EntityList<Category> categories) {
        mCategoryDao.delete(categories
                .getRemoved().stream()
                .map(this::mapID)
                .collect(Collectors.toList()));

        mCategoryDao.upsert(categories
                .getModified().stream()
                .map(category -> mSchemaMapper.mapToSchema(new CategorySchemaMapper.Domain(tripBookId, category)))
                .collect(Collectors.toList()));
    }

    // ---------------------------------------- Query ----------------------------------------------

    public EntityList<Category> getByTripId(ID tripBookId) {
        List<CategorySchema> schemas = mCategoryDao.getByTripId(this.mapID(tripBookId));
        return this.mapSchemasToEntityList(schemas);
    }

    public Observable<EntityList<Category>> fetchByTripId(ID tripBookId) {
        return mCategoryDao
                .fetchByTripId(this.mapID(tripBookId))
                .map(this::mapSchemasToEntityList);
    }

    // region helper method ------------------------------------------------------------------------
    private EntityList<Category> mapSchemasToEntityList(List<CategorySchema> categorySchemas) {
        return EntityList.newList(mSchemaMapper
                .mapToDomain(categorySchemas)
                .stream()
                .map(domain -> domain.mCategory)
                .collect(Collectors.toList()));
    }
    // endregion helper method ---------------------------------------------------------------------
}
