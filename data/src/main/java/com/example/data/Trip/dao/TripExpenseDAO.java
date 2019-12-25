package com.example.data.Trip.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.data.Shared.dao.BaseDao;
import com.example.data.Shared.dao.DaoHelper;
import com.example.data.Trip.schema.tripExpense.TripExpenseSchema;
import com.example.data.Trip.schema.tripbook.TripBookSchema;
import com.example.domain.Trip.tripExpense.TripExpense;

@Dao
public abstract class TripExpenseDAO extends BaseDao<TripExpenseSchema>  {



}
