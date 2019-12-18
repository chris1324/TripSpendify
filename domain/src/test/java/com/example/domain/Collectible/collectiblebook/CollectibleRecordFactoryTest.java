package com.example.domain.Collectible.collectiblebook;

import com.example.domain.Common.sharedvalueobject.date.Date;
import com.example.domain.Common.sharedvalueobject.id.ID;
import com.example.domain.Common.sharedvalueobject.note.Note;
import com.example.domain.Common.sharedvalueobject.numeric.Amount;
import com.example.domain.Common.sharedvalueobject.numeric.MonetaryAmount;
import com.example.domain.Trip.tripexpense.TripExpense;
import com.example.domain.Trip.tripexpense.paymentdetail.PaymentDetail;
import com.example.domain.Trip.tripexpense.splittingdetail.SplittingDetail;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import sun.rmi.runtime.Log;

import static org.hamcrest.core.IsNull.*;
import static org.hamcrest.core.Is.*;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatcher.*;
import static org.mockito.ArgumentCaptor.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CollectibleRecordFactoryTest {

    // region constants ----------------------------------------------------------------------------
    private final ID friend1 = ID.newId();
    private final ID friend2 = ID.newId();
    private final ID friend3 = ID.newId();

    private PaymentDetail mPaymentDetail;
    private SplittingDetail mSplittingDetail;
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------

    // endregion helper fields ---------------------------------------------------------------------

    CollectibleRecordFactory SUT;

    @Before
    public void setup() throws Exception {
        // Wrong - should mock instead of instantiating them as it will also test their behavior
        HashMap<ID, MonetaryAmount> paymentDetailMap = new HashMap<>();
        paymentDetailMap.put(friend1, MonetaryAmount.create(new BigDecimal("50.00")).getValue().get());
        paymentDetailMap.put(friend2, MonetaryAmount.create(new BigDecimal("50.00")).getValue().get());

        HashMap<ID, MonetaryAmount> splitDetailMap = new HashMap<>();
        splitDetailMap.put(friend1, MonetaryAmount.create(new BigDecimal("100.00")).getValue().get());
        splitDetailMap.put(friend2, MonetaryAmount.create(new BigDecimal("100.00")).getValue().get());

        SUT = new CollectibleRecordFactory();
        mPaymentDetail = PaymentDetail.userAndMemberPay(
                MonetaryAmount.create(new BigDecimal("200.00")).getValue().get(),
                paymentDetailMap
        );

        mSplittingDetail = SplittingDetail.userAndMemberSpend(
                MonetaryAmount.create(new BigDecimal("100.00")).getValue().get(),
                splitDetailMap
        );
    }

    @Test
    public void test() throws Exception {
        //Arrange
        //Act
        SUT.create(TripExpense.create(
                ID.newId(),
                ID.newId(),
                Date.create(1576668301657L).getValue().get(),
                Note.create("").getValue().get(),
                MonetaryAmount.create((new BigDecimal("300.00"))).getValue().get(),
                mPaymentDetail,
                mSplittingDetail
        ).getValue().get());

        //Assert
    }

    // region helper methods -----------------------------------------------------------------------

    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------

    // endregion helper classes --------------------------------------------------------------------
}