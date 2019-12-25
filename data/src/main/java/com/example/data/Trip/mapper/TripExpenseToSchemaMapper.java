package com.example.data.Trip.mapper;

import com.example.data.Shared.mapper.SchemaMapper;
import com.example.data.Shared.transaction.TransactionSchema;
import com.example.data.Shared.valueObjectMapper.ValueObjectMapper;
import com.example.data.Trip.schema.tripExpense.MemberPaymentSchema;
import com.example.data.Trip.schema.tripExpense.MemberSpendingSchema;
import com.example.data.Trip.schema.tripExpense.TripExpenseSchema;
import com.example.domain.Shared.errorhanding.exception.ImpossibleState;
import com.example.domain.Shared.valueObject.id.ID;
import com.example.domain.Shared.valueObject.numeric.Amount;
import com.example.domain.Shared.valueObject.numeric.MonetaryAmount;
import com.example.domain.Trip.tripExpense.PaymentDetailFactory;
import com.example.domain.Trip.tripExpense.SplittingDetailFactory;
import com.example.domain.Trip.tripExpense.TripExpense;
import com.example.domain.Trip.tripExpense.paymentdetail.PaymentDetail;
import com.example.domain.Trip.tripExpense.splittingdetail.SplittingDetail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class TripExpenseToSchemaMapper extends SchemaMapper<
        TripExpenseToSchemaMapper.Domain,
        TripExpenseToSchemaMapper.Schema> {

    // region Variables and Constructor ------------------------------------------------------------
    private final ValueObjectMapper mVoMapper;
    private final PaymentDetailFactory mPaymentDetailFactory;
    private final SplittingDetailFactory mSplittingDetailFactory;

    public TripExpenseToSchemaMapper(ValueObjectMapper voMapper,
                                     PaymentDetailFactory paymentDetailFactory,
                                     SplittingDetailFactory splittingDetailFactory) {
        mVoMapper = voMapper;
        mPaymentDetailFactory = paymentDetailFactory;
        mSplittingDetailFactory = splittingDetailFactory;
    }

    // endregion Variables and Constructor ---------------------------------------------------------

    // region Domain -------------------------------------------------------------------------------
    public static class Domain {
        public final ID mTripBookId;
        public final TripExpense mTripExpense;

        public Domain(ID tripBookId, TripExpense tripExpense) {
            mTripBookId = tripBookId;
            mTripExpense = tripExpense;
        }
    }
    // endregion Domain ----------------------------------------------------------------------------

    // region Schema -------------------------------------------------------------------------------
    public static class Schema {
        public final TransactionSchema mTransactionSchema;
        public final TripExpenseSchema mTripExpenseSchema;
        public final List<MemberPaymentSchema> mMemberPaymentSchemas;
        public final List<MemberSpendingSchema> mMemberSpendingSchemas;

        public Schema(TransactionSchema transactionSchema,
                      TripExpenseSchema tripExpenseSchema,
                      List<MemberPaymentSchema> memberPaymentSchemas,
                      List<MemberSpendingSchema> memberSpendingSchemas) {
            mTransactionSchema = transactionSchema;
            mTripExpenseSchema = tripExpenseSchema;
            mMemberPaymentSchemas = memberPaymentSchemas;
            mMemberSpendingSchemas = memberSpendingSchemas;
        }
    }
    // endregion Schema ----------------------------------------------------------------------------

    @Override
    public Schema mapToSchema(Domain domain) {
        return new Schema(
                this.mapTransaction(domain.mTripBookId, domain.mTripExpense),
                this.mapTripExpense(domain.mTripExpense),
                this.mapMembersPayment(domain.mTripExpense),
                this.mapMembersSpending(domain.mTripExpense)
        );
    }

    @Override
    public Domain mapToDomain(Schema schema) {
        return new Domain(
                mVoMapper.mapID(schema.mTransactionSchema.tripBookId),
                TripExpense
                        .create(
                                mVoMapper.mapID(schema.mTransactionSchema.transactionId),
                                mVoMapper.mapID(schema.mTripExpenseSchema.categoryId),
                                mVoMapper.mapDate(schema.mTransactionSchema.date),
                                mVoMapper.mapNote(schema.mTransactionSchema.note),
                                mVoMapper.mapMonetaryAmount(schema.mTransactionSchema.amount),
                                this.mapPaymentDetail(schema),
                                this.mapSplittingDetail(schema))
                        .getValue()
                        .orElseThrow(ImpossibleState::new)
        );
    }

    // region helper method (ToSchema)--------------------------------------------------------------
    private TransactionSchema mapTransaction(ID tripBookId, TripExpense tripExpense) {
        return new TransactionSchema(
                mVoMapper.mapID(tripExpense.getId()),
                mVoMapper.mapID(tripBookId),
                mVoMapper.mapDate(tripExpense.getDate()),
                mVoMapper.mapMonetaryAmount(tripExpense.getAmount()),
                mVoMapper.mapNote(tripExpense.getNote()));
    }

    private TripExpenseSchema mapTripExpense(TripExpense tripExpense) {
        return new TripExpenseSchema(
                mVoMapper.mapID(tripExpense.getId()),
                mVoMapper.mapID(tripExpense.getCategoryId()),
                tripExpense.getPaymentDetail().isUnpaid(),
                this.getUserPayment(tripExpense),
                this.getUserSpending(tripExpense));
    }

    private List<MemberPaymentSchema> mapMembersPayment(TripExpense tripExpense) {
        return tripExpense.getPaymentDetail().getMemberPayment().orElseThrow(ImpossibleState::new)
                .entrySet().stream()
                .map(membersPayment -> new MemberPaymentSchema(
                        mVoMapper.mapID(tripExpense.getId()),
                        mVoMapper.mapID(membersPayment.getKey()),
                        mVoMapper.mapMonetaryAmount(membersPayment.getValue())))
                .collect(Collectors.toList());
    }

    private List<MemberSpendingSchema> mapMembersSpending(TripExpense tripExpense) {
        return tripExpense.getSplittingDetail().getMemberSpending().orElseThrow(ImpossibleState::new)
                .entrySet().stream()
                .map(membersSpending -> new MemberSpendingSchema(
                        mVoMapper.mapID(tripExpense.getId()),
                        mVoMapper.mapID(membersSpending.getKey()),
                        mVoMapper.mapMonetaryAmount(membersSpending.getValue())))
                .collect(Collectors.toList());
    }

    private double getUserPayment(TripExpense tripExpense) {
        return tripExpense.getPaymentDetail()
                .getUserPayment()
                .map(mVoMapper::mapMonetaryAmount)
                .orElse(0.0);
    }

    private double getUserSpending(TripExpense tripExpense) {
        return tripExpense.getSplittingDetail()
                .getUserSpending()
                .map(mVoMapper::mapMonetaryAmount)
                .orElse(0.0);
    }
    // endregion helper method (ToSchema)-----------------------------------------------------------

    // region helper method (ToDomain) -------------------------------------------------------------
    private PaymentDetail mapPaymentDetail(Schema schema) {
        return mPaymentDetailFactory
                .create(mVoMapper.mapAmount(schema.mTripExpenseSchema.userPayment),
                        this.mapMembersPayment(schema),
                        schema.mTripExpenseSchema.isUnpaid)
                .getValue()
                .orElseThrow(ImpossibleState::new);
    }

    private Map<ID, Amount> mapMembersPayment(Schema schema) {
        return schema.mMemberPaymentSchemas.stream().collect(Collectors.toMap(
                memberPaymentSchema -> mVoMapper.mapID(memberPaymentSchema.memberId),
                memberPaymentSchema -> mVoMapper.mapAmount(memberPaymentSchema.amount)));

    }

    private SplittingDetail mapSplittingDetail(Schema schema) {
        return mSplittingDetailFactory
                .create(mVoMapper.mapAmount(schema.mTripExpenseSchema.userSpending),
                        this.mapMembersSpending(schema))
                .getValue()
                .orElseThrow(ImpossibleState::new);
    }

    private Map<ID, Amount> mapMembersSpending(Schema schema) {
        return schema.mMemberSpendingSchemas.stream().collect(Collectors.toMap(
                memberSpendingSchema -> mVoMapper.mapID(memberSpendingSchema.memberId),
                memberSpendingSchema -> mVoMapper.mapAmount(memberSpendingSchema.amount)));
    }
    // endregion helper method (ToDomain) ----------------------------------------------------------


}
