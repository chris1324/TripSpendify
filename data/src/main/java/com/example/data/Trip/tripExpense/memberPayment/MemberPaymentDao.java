package com.example.data.Trip.tripExpense.memberPayment;

import androidx.room.Query;

import com.example.data.Shared.dao.BaseDao;
import com.example.data.Shared.dao.DaoUtility;

import java.util.List;

import io.reactivex.Observable;

public abstract class MemberPaymentDao extends BaseDao<MemberPaymentSchema> {

    @Query("SELECT * FROM member_payment_table WHERE transactionId=:transactionId")
    public abstract List<MemberPaymentSchema> getByTransId(String transactionId);

    @Query("SELECT * FROM member_payment_table WHERE transactionId=:transactionId")
    public abstract Observable<List<MemberPaymentSchema>> fetchByTransId(String transactionId);

}
