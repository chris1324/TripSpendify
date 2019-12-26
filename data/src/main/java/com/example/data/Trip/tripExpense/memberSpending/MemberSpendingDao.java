package com.example.data.Trip.tripExpense.memberSpending;

import androidx.room.Query;

import com.example.data.Shared.dao.BaseDao;
import com.example.data.Shared.dao.DaoUtility;

import java.util.List;

import io.reactivex.Observable;

public abstract class MemberSpendingDao extends BaseDao<MemberSpendingSchema>  {

    @Query("SELECT * FROM member_spending_table WHERE transactionId=:transactionId")
    public abstract List<MemberSpendingSchema> getByTransId(String transactionId);


    @Query("SELECT * FROM member_spending_table WHERE transactionId=:transactionId")
    public abstract Observable<List<MemberSpendingSchema>> fetchByTransId(String transactionId);



}
