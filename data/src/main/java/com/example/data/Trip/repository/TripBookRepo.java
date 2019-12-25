package com.example.data.Trip.repository;

import com.example.application.Trip.repository.TripBookRepository;
import com.example.data.Shared.valueObjectMapper.ValueObjectMapper;
import com.example.data.Trip.dao.TripBookDAO;
import com.example.data.Trip.mapper.TripBookToSchemaMapper;
import com.example.data.Trip.schema.tripbook.TripBookSchema;
import com.example.domain.Shared.domainEventBus.DomainEventBus;
import com.example.domain.Shared.valueObject.id.ID;
import com.example.domain.Trip.tripBook.TripBook;

import java.util.List;

import io.reactivex.Observable;

public class TripBookRepo implements TripBookRepository {

    // region Variables and Constructor ------------------------------------------------------------
    private final TripBookToSchemaMapper mToSchemaMapper;
    private final ValueObjectMapper mVoMapper;

    private final TripBookDAO mTripBookDAO;
    private final TripExpenseRepo mTripExpenseRepo;
    private final TripMemberRepo mTripMemberRepo;
    private final CategoryRepo mCategoryRepo;

    public TripBookRepo(TripBookToSchemaMapper toSchemaMapper, ValueObjectMapper voMapper, TripBookDAO tripBookDAO, TripExpenseRepo tripExpenseRepo, TripMemberRepo tripMemberRepo, CategoryRepo categoryRepo) {
        mToSchemaMapper = toSchemaMapper;
        mVoMapper = voMapper;
        mTripBookDAO = tripBookDAO;
        mTripExpenseRepo = tripExpenseRepo;
        mTripMemberRepo = tripMemberRepo;
        mCategoryRepo = categoryRepo;
    }

    // endregion Variables and Constructor ---------------------------------------------------------

    // ---------------------------------------- Command --------------------------------------------
    @Override
    public void save(TripBook tripBook) {
        mTripBookDAO.upsert(this.mapToSchema(tripBook));
        mTripExpenseRepo.save(tripBook.getId(),tripBook.getTripExpenses());
        mTripMemberRepo.save(tripBook.getId(),tripBook.getTripMembers());
        mCategoryRepo.save(tripBook.getId(),tripBook.getCategories());

        DomainEventBus.getInstance().dispatch(tripBook.getId());
    }

    @Override
    public void remove(ID id) {
        mTripBookDAO.delete(this.mapId(id));
        DomainEventBus.getInstance().dispatch(id);
    }

    // ---------------------------------------- Query ----------------------------------------------
    @Override
    public boolean exist(ID id) {
//        return mTripBookDAO.exist(id.toString());
        return false;
    }

    @Override
    public TripBook getById(ID tripBookId) {
//        return mToSchemaMapper
//                .mapToDomain(mTripBookDAO.getById(tripBookId.toString()));
        return null;
    }

    @Override
    public Observable<TripBook> fetchById(ID tripBookId) {
//        return mTripBookDAO
//                .fetchById(tripBookId.toString())
//                .map(mToSchemaMapper::mapToDomain);
        return null;
    }

    @Override
    public Observable<List<TripBook>> fetchAll() {
//        return mTripBookDAO.fetchAll().map(tripBookSchemas -> tripBookSchemas
//                .stream()
//                .map(mToSchemaMapper::mapToDomain)
//                .collect(Collectors.toList()));
        return null;
    }

    // region helper method ------------------------------------------------------------------------
    private TripBookSchema mapToSchema(TripBook tripBook) {
        return mToSchemaMapper.mapToSchema(tripBook);
    }


    private String mapId(ID id) {
        return mVoMapper.mapID(id);
    }
    // endregion helper method ---------------------------------------------------------------------

}
