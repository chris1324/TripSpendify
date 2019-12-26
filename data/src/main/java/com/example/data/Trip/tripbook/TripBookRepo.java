package com.example.data.Trip.tripbook;

import com.example.application.Trip.tripBook.minimal.TripBookMinimal;
import com.example.application.Trip.tripBook.repository.TripBookRepository;
import com.example.data.Shared.repository.AggregateRootRepository;
import com.example.data.Shared.valueObjectMapper.ValueObjectMapper;
import com.example.data.Trip.Category.CategoryRepo;
import com.example.data.Trip.tripExpense.TripExpenseRepo;
import com.example.data.Trip.tripMember.TripMemberRepo;
import com.example.domain.Shared.valueObject.id.ID;
import com.example.domain.Trip.tripBook.TripBook;

import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.Observable;


public class TripBookRepo extends AggregateRootRepository<TripBook> implements TripBookRepository {

    // region Variables and Constructor ------------------------------------------------------------
    private final TripBookDao mTripBookDao;

    private final CategoryRepo mCategoryRepo;
    private final TripMemberRepo mTripMemberRepo;
    private final TripExpenseRepo mTripExpenseRepo;

    private final TripBookSchemaMapper mTripBookMapper;

    public TripBookRepo(ValueObjectMapper voMapper, TripBookDao tripBookDao, CategoryRepo categoryRepo, TripMemberRepo tripMemberRepo, TripExpenseRepo tripExpenseRepo, TripBookSchemaMapper tripBookMapper) {
        super(voMapper);
        mTripBookDao = tripBookDao;
        mCategoryRepo = categoryRepo;
        mTripMemberRepo = tripMemberRepo;
        mTripExpenseRepo = tripExpenseRepo;
        mTripBookMapper = tripBookMapper;
    }

    // endregion Variables and Constructor ---------------------------------------------------------

    // ---------------------------------------- Command --------------------------------------------
    @Override
    public void save(TripBook tripBook) {
        final TripBookSchemaMapper.Schema schema = mTripBookMapper.mapToSchema(tripBook);

        mTripBookDao.upsert(schema.mTripBookSchema);
        mCategoryRepo.save(tripBook.getId(), schema.mCategories);
        mTripMemberRepo.save(tripBook.getId(), schema.mTripMembers);
        mTripExpenseRepo.save(tripBook.getId(), schema.mTripExpenses);
        super.save(tripBook);

    }


    @Override
    public void remove(ID id) {
        mTripBookDao.delete(this.mapID(id));
        super.remove(id);
    }


    // ---------------------------------------- Query ----------------------------------------------

    @Override
    public TripBook getById(ID tripBookId) {
        return mTripBookMapper.mapToDomain(new TripBookSchemaMapper.Schema(
                mTripBookDao.getById(this.mapID(tripBookId)),
                mCategoryRepo.getByTripId(tripBookId),
                mTripMemberRepo.getByTripId(tripBookId),
                mTripExpenseRepo.getByTripId(tripBookId)));
    }

    @Override
    public boolean exist(ID id) {
        return mTripBookDao.exist(this.mapID(id));
    }

    @Override
    public Observable<TripBook> fetchById(ID tripBookId) {
        return Observable.combineLatest(
                mTripBookDao.fetchById(this.mapID(tripBookId)),
                mCategoryRepo.fetchByTripId(tripBookId),
                mTripMemberRepo.fetchByTripId(tripBookId),
                mTripExpenseRepo.fetchByTripId(tripBookId),
                (tripBookSchema, categories, tripMembers, tripExpenses) ->
                        mTripBookMapper.mapToDomain(new TripBookSchemaMapper.Schema(
                                tripBookSchema,
                                categories,
                                tripMembers,
                                tripExpenses)));
    }

    @Override
    public Observable<TripBookMinimal> fetchMinimalById(ID tripBookId) {
        return mTripBookDao
                .fetchById(this.mapID(tripBookId))
                .map(this::mapTripBookMinimal);
    }

    @Override
    public Observable<List<TripBookMinimal>> fetchAllMinimal() {
        return mTripBookDao
                .fetchAll()
                .map(tripBookSchemas -> tripBookSchemas.stream()
                        .map(this::mapTripBookMinimal)
                        .collect(Collectors.toList()));
    }

    // region helper method ------------------------------------------------------------------------
    private TripBookMinimal mapTripBookMinimal(TripBookSchema tripBookSchema) {
        return new TripBookMinimal(
                this.mapID(tripBookSchema.tripBookId),
                mVoMapper.mapName(tripBookSchema.tripName),
                mVoMapper.mapUri(tripBookSchema.photoUri),
                mVoMapper.mapDate(tripBookSchema.startDate),
                mVoMapper.mapDate(tripBookSchema.endDate));
    }
    // endregion helper method ---------------------------------------------------------------------

}
