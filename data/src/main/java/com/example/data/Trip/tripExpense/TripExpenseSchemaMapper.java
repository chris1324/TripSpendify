package com.example.data.Trip.tripExpense;

import com.example.data.Shared.mapper.SchemaMapper;
import com.example.data.Shared.mapper.TransactionSchemaMapper;
import com.example.data.Shared.transaction.TransactionSchema;
import com.example.data.Shared.valueObjectMapper.ValueObjectMapper;
import com.example.data.Trip.tripExpense.memberPayment.MemberPaymentSchema;
import com.example.data.Trip.tripExpense.memberSpending.MemberSpendingSchema;
import com.example.domain.Shared.errorhanding.exception.ImpossibleState;
import com.example.domain.Shared.valueObject.id.ID;
import com.example.domain.Shared.valueObject.numeric.Amount;
import com.example.domain.Shared.valueObject.numeric.MonetaryAmount;
import com.example.domain.Trip.tripExpense.PaymentDetailFactory;
import com.example.domain.Trip.tripExpense.SplittingDetailFactory;
import com.example.domain.Trip.tripExpense.TripExpense;
import com.example.domain.Trip.tripExpense.paymentdetail.PaymentDetail;
import com.example.domain.Trip.tripExpense.splittingdetail.SplittingDetail;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TripExpenseSchemaMapper extends TransactionSchemaMapper<
        TripExpenseSchemaMapper.Domain,
        TripExpenseSchemaMapper.Schema> {

    // region Variables and Constructor ------------------------------------------------------------
    private final PaymentDetailFactory mPaymentDetailFactory;
    private final SplittingDetailFactory mSplittingDetailFactory;

    public TripExpenseSchemaMapper(ValueObjectMapper voMapper,
                                   PaymentDetailFactory paymentDetailFactory, 
                                   SplittingDetailFactory splittingDetailFactory) {
        super(voMapper);
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
                this.mapMembersPayment(domain.mTripExpense.getId(), domain.mTripExpense.getPaymentDetail()),
                this.mapMembersSpending(domain.mTripExpense.getId(), domain.mTripExpense.getSplittingDetail())
        );
    }

    private TripExpenseSchema mapTripExpense(TripExpense tripExpense) {
        return new TripExpenseSchema(
                this.mapID(tripExpense.getId()),
                this.mapID(tripExpense.getCategoryId()),
                tripExpense.getPaymentDetail().isUnpaid(),
                this.getUserPayment(tripExpense),
                this.getUserSpending(tripExpense)
        );
    }

    private List<MemberPaymentSchema> mapMembersPayment(ID tripExpenseId, PaymentDetail paymentDetail) {
        return paymentDetail.getMemberPayment()
                .map(membersPaymentMap -> membersPaymentMap
                        .entrySet().stream()
                        .map(memberPayment -> this.mapMemberPayment(tripExpenseId, memberPayment))
                        .collect(Collectors.toList()))
                .orElseGet(ArrayList::new);
    }

    private List<MemberSpendingSchema> mapMembersSpending(ID tripExpenseId, SplittingDetail splittingDetail) {
        return splittingDetail.getMemberSpending()
                .map(membersSpendingMap -> membersSpendingMap
                        .entrySet().stream()
                        .map(memberSpending -> this.mapMemberSpending(tripExpenseId, memberSpending))
                        .collect(Collectors.toList()))
                .orElseGet(ArrayList::new);
    }

    // region helper method (mapToSchema) ----------------------------------------------------------
    private MemberPaymentSchema mapMemberPayment(ID tripExpenseId, Map.Entry<ID, MonetaryAmount> memberPayment) {
        return new MemberPaymentSchema(
                this.mapID(tripExpenseId),
                this.mapID(memberPayment.getKey()),
                this.mapMonetaryAmount(memberPayment.getValue())
        );
    }

    private MemberSpendingSchema mapMemberSpending(ID tripExpenseId, Map.Entry<ID, MonetaryAmount> memberSpending) {
        return new MemberSpendingSchema(
                this.mapID(tripExpenseId),
                this.mapID(memberSpending.getKey()),
                this.mapMonetaryAmount(memberSpending.getValue())
        );
    }

    private double getUserPayment(TripExpense tripExpense) {
        return tripExpense.getPaymentDetail()
                .getUserPayment()
                .map(this::mapMonetaryAmount)
                .orElse(0.0);
    }

    private double getUserSpending(TripExpense tripExpense) {
        return tripExpense.getSplittingDetail()
                .getUserSpending()
                .map(this::mapMonetaryAmount)
                .orElse(0.0);
    }
    // endregion helper method (mapToSchema) -------------------------------------------------------

    @Override
    public Domain mapToDomain(Schema schema) {
        return new Domain(
                this.mapID(schema.mTransactionSchema.tripBookId),
                this.mapTripExpense(schema));
    }

    private TripExpense mapTripExpense(Schema schema) {
        return TripExpense
                .create(this.mapID(schema.mTransactionSchema.transactionId),
                        this.mapID(schema.mTripExpenseSchema.categoryId),
                        this.mapDate(schema.mTransactionSchema.date),
                        this.mapNote(schema.mTransactionSchema.note),
                        this.mapMonetaryAmount(schema.mTransactionSchema.amount),
                        this.mapPaymentDetail(schema.mTripExpenseSchema, schema.mMemberPaymentSchemas),
                        this.mapSplittingDetail(schema.mTripExpenseSchema, schema.mMemberSpendingSchemas))
                .getValue()
                .orElseThrow(ImpossibleState::new);
    }

    private PaymentDetail mapPaymentDetail(TripExpenseSchema tripExpenseSchema, List<MemberPaymentSchema> memberPaymentSchemas) {
        return mPaymentDetailFactory
                .create(this.mapAmount(tripExpenseSchema.userPayment),
                        this.mapMembersPayment(memberPaymentSchemas),
                        tripExpenseSchema.isUnpaid)
                .getValue()
                .orElseThrow(ImpossibleState::new);
    }

    private SplittingDetail mapSplittingDetail(TripExpenseSchema tripExpenseSchema, List<MemberSpendingSchema> memberSpendingSchemas) {
        return mSplittingDetailFactory
                .create(this.mapAmount(tripExpenseSchema.userSpending),
                        this.mapMembersSpending(memberSpendingSchemas))
                .getValue()
                .orElseThrow(ImpossibleState::new);
    }

    // region helper method (mapToDomain) ----------------------------------------------------------
    private Map<ID, Amount> mapMembersPayment(List<MemberPaymentSchema> memberPaymentSchemas) {
        return memberPaymentSchemas.stream().collect(Collectors.toMap(
                schema -> this.mapID(schema.memberId),
                schema -> this.mapAmount(schema.amount)));
    }

    private Map<ID, Amount> mapMembersSpending(List<MemberSpendingSchema>  memberSpendingSchemas) {
        return memberSpendingSchemas.stream().collect(Collectors.toMap(
                schema -> this.mapID(schema.memberId),
                schema -> this.mapAmount(schema.amount)));
    }
    // endregion helper method (mapToDomain) -------------------------------------------------------


}
