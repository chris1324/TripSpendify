package com.example.application.Trip.tripExpense;

import com.example.application.Common.repository.TripBookRepository;
import com.example.application.Common.usecase.CommandUseCase;
import com.example.domain.Common.errorhanding.exception.ImpossibleState;
import com.example.domain.Common.errorhanding.exception.UnhandledError;
import com.example.domain.Common.errorhanding.outcome.Outcome;
import com.example.domain.Common.errorhanding.result.Result;
import com.example.domain.Common.sharedvalueobject.date.Date;
import com.example.domain.Common.sharedvalueobject.id.ID;
import com.example.domain.Common.sharedvalueobject.note.Note;
import com.example.domain.Common.sharedvalueobject.numeric.Amount;
import com.example.domain.Common.sharedvalueobject.numeric.MonetaryAmount;
import com.example.domain.Trip.tripbook.TripBook;
import com.example.domain.Trip.tripexpense.PaymentDetailFactory;
import com.example.domain.Trip.tripexpense.SplittingDetailFactory;
import com.example.domain.Trip.tripexpense.TripExpense;
import com.example.domain.Trip.tripexpense.paymentdetail.PaymentDetail;
import com.example.domain.Trip.tripexpense.splittingdetail.SplittingDetail;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class AddTripExpense extends CommandUseCase<AddTripExpense.RequestModel, AddTripExpense.Err> {

    // region Variables and Constructor ------------------------------------------------------------
    private final PaymentDetailFactory mPaymentDetailFactory;
    private final SplittingDetailFactory mSplittingDetailFactory;
    private final TripBookRepository mTripBookRepository;

    public AddTripExpense(PaymentDetailFactory paymentDetailFactory,
                          SplittingDetailFactory splittingDetailFactory,
                          TripBookRepository tripBookRepository) {
        mPaymentDetailFactory = paymentDetailFactory;
        mSplittingDetailFactory = splittingDetailFactory;
        mTripBookRepository = tripBookRepository;
    }

    // endregion Variables and Constructor ---------------------------------------------------------

    // region RequestModel  ------------------------------------------------------------------------
    public static class RequestModel {
        final String tripBookId;
        final String categoryId;
        final long date;
        final String note;
        final String totalExpense;
        final boolean isUnpaid;
        final String amountPaidByUser;
        final String amountSpentByUser;
        final Map<String, String> amountPaidByMember;
        final Map<String, String> amountSpentByMember;

        public RequestModel(String tripBookId,
                            String categoryId,
                            long date,
                            String note,
                            String totalExpense,
                            boolean isUnpaid,
                            String amountPaidByUser,
                            String amountSpentByUser,
                            Map<String, String> amountPaidByMember,
                            Map<String, String> amountSpentByMember) {
            this.tripBookId = tripBookId;
            this.categoryId = categoryId;
            this.date = date;
            this.note = note;
            this.totalExpense = totalExpense;
            this.isUnpaid = isUnpaid;
            this.amountPaidByUser = amountPaidByUser;
            this.amountSpentByUser = amountSpentByUser;
            this.amountPaidByMember = amountPaidByMember;
            this.amountSpentByMember = amountSpentByMember;
        }
    }
    // endregion RequestModel  ---------------------------------------------------------------------

    @Override
    protected Outcome<Err> execute(RequestModel requestModel) {
        // Attributes
        // -- prepare
        Amount amountPaidByUser = Amount
                .create(requestModel.amountPaidByUser)
                .getValue()
                .orElse(Amount.createZeroAmount());
        Amount amountSpentByUser = Amount
                .create(requestModel.amountSpentByUser)
                .getValue()
                .orElse(Amount.createZeroAmount());
        Map<ID, Amount> amountPaidByMember = this.createAmountsExcludeInvalid(requestModel.amountPaidByMember);
        Map<ID, Amount> amountSpentByMember = this.createAmountsExcludeInvalid(requestModel.amountSpentByMember);

        // -- create
        Result<Date, Date.Err.Create> date = Date.create(requestModel.date);
        Result<Note, Note.Err.Create> note = Note.create(requestModel.note);
        Result<MonetaryAmount, MonetaryAmount.Err.Create> totalExpense = MonetaryAmount.create(new BigDecimal(requestModel.totalExpense));
        Result<PaymentDetail, PaymentDetailFactory.Err> paymentDetail = mPaymentDetailFactory.create(
                amountPaidByUser,
                amountPaidByMember,
                requestModel.isUnpaid);
        Result<SplittingDetail, SplittingDetailFactory.Err> splittingDetail = mSplittingDetailFactory.create(
                amountSpentByUser,
                amountSpentByMember);

        // -- check and handle Err
        if (date.isErr()) return this.handleDateErr(date);
        if (note.isErr()) return this.handleNoteErr(note);
        if (totalExpense.isErr()) return this.handleTotalExpenseErr(totalExpense);
        if (paymentDetail.isErr()) return this.handlePaymentDetailErr(paymentDetail);
        if (splittingDetail.isErr()) return this.handleSplittingDetailErr(splittingDetail);

        // TripExpense
        // -- create
        Result<TripExpense, TripExpense.Err.Create> tripExpense = TripExpense.create(
                ID.newId(),
                ID.existingId(requestModel.categoryId),
                date.getValue().orElseThrow(ImpossibleState::new),
                note.getValue().orElseThrow(ImpossibleState::new),
                totalExpense.getValue().orElseThrow(ImpossibleState::new),
                paymentDetail.getValue().orElseThrow(ImpossibleState::new),
                splittingDetail.getValue().orElseThrow(ImpossibleState::new)
        );

        // -- check and handle Err
        if (tripExpense.isErr()) return this.handleTripExpenseErr(tripExpense);

        // TripBook
        // -- get from repo & check if exist
        TripBook tripBook = mTripBookRepository.getTripBookById(ID.existingId(requestModel.tripBookId));
        if (tripBook == null) return Outcome.err(Err.TRIP_ID_INVALID);

        // -- addExpense
        Outcome<TripBook.Err.AddExpense> outcome = tripBook.addExpense(tripExpense
                .getValue()
                .orElseThrow(ImpossibleState::new));

        // -- check and handle Error
        if (outcome.isErr()) return this.handleAddTripExpenseErr(outcome);

        // Success
        return Outcome.ok();
    }

    // region helper method ------------------------------------------------------------------------
    private Map<ID, Amount> createAmountsExcludeInvalid(Map<String, String> amounts) {
        Map<ID, Amount> amountsInAmt = new HashMap<>();
        for (String id : amounts.keySet()) {
            Result<Amount, Amount.Err.Create> amountInAmt = Amount.create(amounts.get(id));

            if (amountInAmt.isErr()) break;

            amountsInAmt.put(ID.existingId(id), amountInAmt
                    .getValue()
                    .orElseThrow(ImpossibleState::new));
        }

        return amountsInAmt;
    }
    // endregion helper method ---------------------------------------------------------------------

    // region Error handing ------------------------------------------------------------------------
    public enum Err {
        TRIP_ID_INVALID,
        DATE_NULL,
        NOTE_NULL,
        TOTAL_NULL,
        TOTAL_NEGATIVE,
        TOTAL_ZERO,
        NO_ONE_SPENDING,
        NO_ONE_PAYING,
        PAYMENT_TOTAL_NOT_TALLY,
        SPLIT_TOTAL_NOT_TALLY,
        PAYER_INVALID,
        SPENDER_INVALID,
        CATEGORY_INVALID,
        USER_NOT_INVOLVED
    }

    private Outcome<Err> handleTripExpenseErr(Result<TripExpense, TripExpense.Err.Create> tripExpense) {
        switch (tripExpense.getError().orElseThrow(ImpossibleState::new)) {
            case NULL_ARGUMENT:
                throw new ImpossibleState();
            case PAYMENT_TOTAL_NOT_TALLY:
                return Outcome.err(Err.PAYMENT_TOTAL_NOT_TALLY);
            case SPLIT_TOTAL_NOT_TALLY:
                return Outcome.err(Err.SPLIT_TOTAL_NOT_TALLY);
            default:
                throw new UnhandledError();
        }
    }

    private Outcome<Err> handleAddTripExpenseErr(Outcome<TripBook.Err.AddExpense> outcome) {
        switch (outcome.getError().orElseThrow(ImpossibleState::new)) {
            case PAYER_INVALID:
                return Outcome.err(Err.PAYER_INVALID);
            case SPENDER_INVALID:
                return Outcome.err(Err.SPENDER_INVALID);
            case CATEGORY_INVALID:
                return Outcome.err(Err.CATEGORY_INVALID);
            case USER_NOT_INVOLVED:
                return Outcome.err(Err.USER_NOT_INVOLVED);
            default:
                throw new UnhandledError();
        }
    }

    private Outcome<Err> handleTotalExpenseErr(Result<MonetaryAmount, MonetaryAmount.Err.Create> totalExpense) {
        switch (totalExpense.getError().orElseThrow(ImpossibleState::new)) {
            case AMOUNT_NULL:
                return Outcome.err(Err.TOTAL_NULL);
            case AMOUNT_NEGATIVE:
                return Outcome.err(Err.TOTAL_NEGATIVE);
            case AMOUNT_ZERO:
                return Outcome.err(Err.TOTAL_ZERO);
            default:
                throw new UnhandledError();
        }
    }

    private Outcome<Err> handleDateErr(Result<Date, Date.Err.Create> date) {
        switch (date.getError().orElseThrow(ImpossibleState::new)) {
            case MILLISECOND_IS_ZERO:
                return Outcome.err(Err.DATE_NULL);
            default:
                throw new UnhandledError();
        }
    }

    private Outcome<Err> handleNoteErr(Result<Note, Note.Err.Create> note) {
        switch (note.getError().orElseThrow(ImpossibleState::new)) {
            case NULL_NOTE:
                return Outcome.err(Err.NOTE_NULL);
            default:
                throw new UnhandledError();
        }
    }

    private Outcome<Err> handleSplittingDetailErr(Result<SplittingDetail, SplittingDetailFactory.Err> splittingDetail) {
        switch (splittingDetail.getError().orElseThrow(ImpossibleState::new)) {
            case NO_ONE_SPENDING:
                return Outcome.err(Err.NO_ONE_SPENDING);
            default:
                throw new UnhandledError();
        }
    }

    private Outcome<Err> handlePaymentDetailErr(Result<PaymentDetail, PaymentDetailFactory.Err> paymentDetail) {
        switch (paymentDetail.getError().orElseThrow(ImpossibleState::new)) {
            case NO_ONE_PAYING:
                return Outcome.err(Err.NO_ONE_PAYING);
            default:
                throw new UnhandledError();
        }
    }

    // endregion Error handing ---------------------------------------------------------------------
}
