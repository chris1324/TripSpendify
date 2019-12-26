package com.example.application.Trip.tripExpense.usecase;

import com.example.application.Shared.baseUseCase.useCase.UseCase;
import com.example.application.Trip.tripBook.repository.TripBookRepository;
import com.example.application.Shared.baseUseCase.commandUseCase.CommandUseCase;
import com.example.application.Trip.tripExpense.dto.TripExpenseDTO;
import com.example.domain.Shared.errorhanding.exception.ImpossibleState;
import com.example.domain.Shared.errorhanding.exception.UnhandledError;
import com.example.domain.Shared.errorhanding.outcome.Outcome;
import com.example.domain.Shared.errorhanding.result.Result;
import com.example.domain.Shared.valueObject.date.Date;
import com.example.domain.Shared.valueObject.id.ID;
import com.example.domain.Shared.valueObject.note.Note;
import com.example.domain.Shared.valueObject.numeric.Amount;
import com.example.domain.Shared.valueObject.numeric.MonetaryAmount;
import com.example.domain.Trip.tripBook.TripBook;
import com.example.domain.Trip.tripExpense.PaymentDetailFactory;
import com.example.domain.Trip.tripExpense.SplittingDetailFactory;
import com.example.domain.Trip.tripExpense.TripExpense;
import com.example.domain.Trip.tripExpense.paymentdetail.PaymentDetail;
import com.example.domain.Trip.tripExpense.splittingdetail.SplittingDetail;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class SaveTripExpense extends CommandUseCase<SaveTripExpense.RequestDTO, SaveTripExpense.Err> {

    // region Variables and Constructor ------------------------------------------------------------
    private final PaymentDetailFactory mPaymentDetailFactory;
    private final SplittingDetailFactory mSplittingDetailFactory;
    private final TripBookRepository mTripBookRepository;

    public SaveTripExpense(PaymentDetailFactory paymentDetailFactory,
                           SplittingDetailFactory splittingDetailFactory,
                           TripBookRepository tripBookRepository) {
        mPaymentDetailFactory = paymentDetailFactory;
        mSplittingDetailFactory = splittingDetailFactory;
        mTripBookRepository = tripBookRepository;
    }

    // endregion Variables and Constructor ---------------------------------------------------------

    // region RequestDTO  ------------------------------------------------------------------------
    public static class RequestDTO implements UseCase.RequestDTO {
        public final String tripBookId;
        public final TripExpenseDTO mTripExpenseDTO;

        public RequestDTO(String tripBookId, TripExpenseDTO tripExpenseDTO) {
            this.tripBookId = tripBookId;
            mTripExpenseDTO = tripExpenseDTO;
        }
    }
    // endregion RequestDTO  ---------------------------------------------------------------------

    @Override
    protected Outcome<Err> execute(RequestDTO requestDTO) {
        TripExpenseDTO tripExpenseDTO = requestDTO.mTripExpenseDTO;
        // Attributes
        // -- prepare
        ID tripExpenseId = this.getTripExpenseID(requestDTO);
        Amount amountPaidByUser = Amount
                .create(tripExpenseDTO.amountPaidByUser)
                .getValue()
                .orElse(Amount.createZeroAmount());
        Amount amountSpentByUser = Amount
                .create(tripExpenseDTO.amountSpentByUser)
                .getValue()
                .orElse(Amount.createZeroAmount());
        Map<ID, Amount> amountPaidByMember = this.createAmountsFilterNull(tripExpenseDTO.amountPaidByMember);
        Map<ID, Amount> amountSpentByMember = this.createAmountsFilterNull(tripExpenseDTO.amountSpentByMember);

        // -- create
        Result<Date, Date.Err.Create> date = Date.create(tripExpenseDTO.date);
        Result<Note, Note.Err.Create> note = Note.create(tripExpenseDTO.note);
        Result<MonetaryAmount, MonetaryAmount.Err.Create> totalExpense = MonetaryAmount.create(new BigDecimal(tripExpenseDTO.totalExpense));
        Result<PaymentDetail, PaymentDetailFactory.Err> paymentDetail = mPaymentDetailFactory.create(
                amountPaidByUser,
                amountPaidByMember,
                tripExpenseDTO.isUnpaid);
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
                tripExpenseId,
                ID.existingId(tripExpenseDTO.categoryId),
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
        TripBook tripBook = mTripBookRepository.getById(ID.existingId(requestDTO.tripBookId));
        if (tripBook == null) return Outcome.err(Err.TRIP_ID_INVALID);

        // -- saveExpense
        Outcome<TripBook.Err.SaveExpense> outcome = tripBook.saveExpense(tripExpense
                .getValue()
                .orElseThrow(ImpossibleState::new));

        // -- check and handle Error
        if (outcome.isErr()) return this.handleSaveTripExpenseErr(outcome);

        // Success
        return Outcome.ok();
    }


    // region helper method ------------------------------------------------------------------------
    private ID getTripExpenseID(RequestDTO requestDTO) {
         String tripExpenseId = requestDTO.mTripExpenseDTO.tripExpenseId;

        if (tripExpenseId == null)  return ID.newId();
        return ID.existingId(tripExpenseId);
    }

    private Map<ID, Amount> createAmountsFilterNull(Map<String, String> amounts) {
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
    public enum Err implements UseCase.ResponseDTO {
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

    private Outcome<Err> handleSaveTripExpenseErr(Outcome<TripBook.Err.SaveExpense> outcome) {
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
