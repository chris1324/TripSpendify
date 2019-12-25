package com.example.data.Trip.repository;

import com.example.application.Shared.repository.Repository;
import com.example.data.Shared.transaction.TransactionDAO;
import com.example.data.Shared.valueObjectMapper.ValueObjectMapper;
import com.example.data.Trip.dao.MemberPaymentDAO;
import com.example.data.Trip.dao.MemberSpendingDAO;
import com.example.data.Trip.dao.TripExpenseDAO;
import com.example.data.Trip.mapper.TripExpenseToSchemaMapper;
import com.example.domain.Shared.entityList.EntityList;
import com.example.domain.Shared.valueObject.id.ID;
import com.example.domain.Trip.tripExpense.TripExpense;

import java.util.List;
import java.util.stream.Collectors;

public class TripExpenseRepo {

    // region Variables and Constructor ------------------------------------------------------------
    private final TripExpenseToSchemaMapper mMapper;
    private final ValueObjectMapper mVoMapper;

    private final TripExpenseDAO mTripExpenseDao;
    private final TransactionDAO mTransactionDAO;
    private final MemberPaymentDAO mMemberPaymentDAO;
    private final MemberSpendingDAO mMemberSpendingDAO;

    public TripExpenseRepo(TripExpenseToSchemaMapper mapper,
                           ValueObjectMapper voMapper,
                           TripExpenseDAO tripExpenseDao,
                           TransactionDAO transactionDAO,
                           MemberPaymentDAO memberPaymentDAO,
                           MemberSpendingDAO memberSpendingDAO) {
        mMapper = mapper;
        mVoMapper = voMapper;
        mTripExpenseDao = tripExpenseDao;
        mTransactionDAO = transactionDAO;
        mMemberPaymentDAO = memberPaymentDAO;
        mMemberSpendingDAO = memberSpendingDAO;
    }

    // endregion Variables and Constructor ---------------------------------------------------------

    // ---------------------------------------- Command --------------------------------------------
    public void save(ID tripBookId, EntityList<TripExpense> tripExpenses) {
        this.remove(tripExpenses
                .getRemoved().stream()
                .map(mVoMapper::mapID)
                .collect(Collectors.toList()));

        this.save(tripExpenses
                .getModified().stream()
                .map(tripExpense -> mMapper.mapToSchema(new TripExpenseToSchemaMapper.Domain(tripBookId, tripExpense)))
                .collect(Collectors.toList()));
    }

    private void save(List<TripExpenseToSchemaMapper.Schema> schemas) {
        schemas.forEach(schema -> {
            mTransactionDAO.upsert(schema.mTransactionSchema);
            mTripExpenseDao.upsert(schema.mTripExpenseSchema);
            mMemberPaymentDAO.upsert(schema.mMemberPaymentSchemas);
            mMemberSpendingDAO.upsert(schema.mMemberSpendingSchemas);
        });
    }

    private void remove(List<String> ids) {
        mTransactionDAO.delete(ids);
    }
    // ---------------------------------------- Query ----------------------------------------------


}
