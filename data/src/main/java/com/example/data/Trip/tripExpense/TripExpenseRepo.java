package com.example.data.Trip.tripExpense;

import com.example.data.Shared.repository.BaseRepository;
import com.example.data.Shared.transaction.TransactionDao;
import com.example.data.Shared.transaction.TransactionSchema;
import com.example.data.Shared.valueObjectMapper.ValueObjectMapper;
import com.example.data.Trip.tripExpense.memberPayment.MemberPaymentDao;
import com.example.data.Trip.tripExpense.memberPayment.MemberPaymentSchema;
import com.example.data.Trip.tripExpense.memberSpending.MemberSpendingDao;
import com.example.data.Trip.tripExpense.memberSpending.MemberSpendingSchema;
import com.example.domain.Shared.entityList.EntityList;
import com.example.domain.Shared.valueObject.id.ID;
import com.example.domain.Trip.tripExpense.TripExpense;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.reactivex.Observable;

public class TripExpenseRepo extends BaseRepository {

    // region Variables and Constructor ------------------------------------------------------------
    private final TripExpenseDao mTripExpenseDao;
    private final TransactionDao mTransactionDao;
    private final MemberPaymentDao mMemberPaymentDao;
    private final MemberSpendingDao mMemberSpendingDao;

    private final TripExpenseSchemaMapper mSchemaMapper;

    public TripExpenseRepo(TripExpenseDao tripExpenseDao,
                           TransactionDao transactionDao,
                           MemberPaymentDao memberPaymentDao,
                           MemberSpendingDao memberSpendingDao,
                           TripExpenseSchemaMapper schemaMapper,
                           ValueObjectMapper voMapper) {
        super(voMapper);
        mTripExpenseDao = tripExpenseDao;
        mTransactionDao = transactionDao;
        mMemberPaymentDao = memberPaymentDao;
        mMemberSpendingDao = memberSpendingDao;
        mSchemaMapper = schemaMapper;
    }

    // endregion Variables and Constructor ---------------------------------------------------------

    // ---------------------------------------- Command --------------------------------------------
    public void save(ID tripBookId, EntityList<TripExpense> tripExpenses) {
        mTripExpenseDao.delete(tripExpenses
                .getRemoved().stream()
                .map(this::mapID)
                .collect(Collectors.toList()));

        tripExpenses.getModified().stream()
                .map(tripExpense -> mSchemaMapper.mapToSchema(new TripExpenseSchemaMapper.Domain(tripBookId, tripExpense)))
                .forEach(schema -> {
                            mTransactionDao.upsert(schema.mTransactionSchema);
                            mTripExpenseDao.upsert(schema.mTripExpenseSchema);
                            mMemberPaymentDao.upsert(schema.mMemberPaymentSchemas);
                            mMemberSpendingDao.upsert(schema.mMemberSpendingSchemas);
                        }
                );
    }

    // ---------------------------------------- Query ----------------------------------------------
    public EntityList<TripExpense> getByTripId(ID tripBookId) {
        Stream<TripExpense> schemas = mTransactionDao.getByTripId(this.mapID(tripBookId)).stream()
                .map(schema -> this.mapTripExpense(
                        schema,
                        mTripExpenseDao.getById(schema.transactionId),
                        mMemberPaymentDao.getByTransId(schema.transactionId),
                        mMemberSpendingDao.getByTransId(schema.transactionId)));

        return this.mapArrayToEntityList(schemas.toArray());
    }

    public Observable<EntityList<TripExpense>> fetchByTripId(ID tripBookId) {
        return mTransactionDao
                .fetchByTripId(this.mapID(tripBookId))
                .flatMap(this::fetchTripExpenses);

    }

    // region helper method ------------------------------------------------------------------------
    private Observable<EntityList<TripExpense>> fetchTripExpenses(List<TransactionSchema> transactionSchemas) {
        return Observable.combineLatest(
                transactionSchemas.stream().map(this::fetchTripExpense).collect(Collectors.toList()),
                this::mapArrayToEntityList);
    }

    private Observable<TripExpense> fetchTripExpense(TransactionSchema transactionSchema) {
        return Observable.combineLatest(
                mTripExpenseDao.fetchById(transactionSchema.transactionId),
                mMemberPaymentDao.fetchByTransId(transactionSchema.transactionId),
                mMemberSpendingDao.fetchByTransId(transactionSchema.transactionId),
                (tripExpenseSchema, memberPaymentSchemas, memberSpendingSchemas) ->
                        this.mapTripExpense(transactionSchema, tripExpenseSchema, memberPaymentSchemas, memberSpendingSchemas));
    }

    // ---------------------------------------------------------------------------------------------
    private TripExpense mapTripExpense(TransactionSchema transactionSchema,
                                       TripExpenseSchema tripExpenseSchema,
                                       List<MemberPaymentSchema> memberPaymentSchemas,
                                       List<MemberSpendingSchema> memberSpendingSchemas) {
        return mSchemaMapper
                .mapToDomain(new TripExpenseSchemaMapper.Schema(
                        transactionSchema,
                        tripExpenseSchema,
                        memberPaymentSchemas,
                        memberSpendingSchemas))
                .mTripExpense;
    }

    private EntityList<TripExpense> mapArrayToEntityList(Object[] tripExpenses) {
        return EntityList.newList(Arrays.asList((TripExpense[]) tripExpenses));
    }

    // endregion helper method ---------------------------------------------------------------------

}
