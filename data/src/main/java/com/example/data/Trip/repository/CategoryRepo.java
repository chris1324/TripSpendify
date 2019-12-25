package com.example.data.Trip.repository;

import com.example.application.Shared.repository.Repository;
import com.example.data.Shared.valueObjectMapper.ValueObjectMapper;
import com.example.data.Trip.dao.CategoryDAO;
import com.example.data.Trip.mapper.CategoryToMemberMapper;
import com.example.domain.Cash.cashBook.CashBook;
import com.example.domain.Shared.entityList.EntityList;
import com.example.domain.Shared.valueObject.id.ID;
import com.example.domain.Trip.category.Category;

import java.util.stream.Collectors;

public class CategoryRepo {

    // region Variables and Constructor ------------------------------------------------------------
    private final CategoryDAO mCategoryDAO;
    private final CategoryToMemberMapper mMapper;
    private final ValueObjectMapper mVoMapper;

    public CategoryRepo(CategoryDAO categoryDAO, CategoryToMemberMapper mapper, ValueObjectMapper voMapper) {
        mCategoryDAO = categoryDAO;
        mMapper = mapper;
        mVoMapper = voMapper;
    }

    // endregion Variables and Constructor ---------------------------------------------------------

    // ---------------------------------------- Command --------------------------------------------
    public void save(ID tripBookId, EntityList<Category> categories) {
        mCategoryDAO.delete(categories
                .getRemoved().stream()
                .map(mVoMapper::mapID)
                .collect(Collectors.toList()));

        mCategoryDAO.upsert(categories
                .getModified().stream()
                .map(category -> mMapper.mapToSchema(new CategoryToMemberMapper.Domain(tripBookId, category)))
                .collect(Collectors.toList()));
    }

    // ---------------------------------------------------------------------------------------------
}
