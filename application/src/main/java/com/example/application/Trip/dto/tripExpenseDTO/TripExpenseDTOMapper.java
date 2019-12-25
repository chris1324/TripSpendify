package com.example.application.Trip.dto.tripExpenseDTO;

import com.example.application.Shared.dtoMapper.DtoMapper;
import com.example.domain.Shared.errorhanding.exception.ImpossibleState;
import com.example.domain.Shared.errorhanding.exception.UnexpectedEnumValue;
import com.example.domain.Shared.valueObject.id.ID;
import com.example.domain.Shared.valueObject.numeric.Amount;
import com.example.domain.Shared.valueObject.numeric.MonetaryAmount;
import com.example.domain.Trip.tripExpense.TripExpense;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class TripExpenseDTOMapper implements DtoMapper<TripExpense, TripExpenseDTO> {

    @Override
    public TripExpenseDTO mapToDto(TripExpense tripExpense) {
        return new TripExpenseDTO(
                tripExpense.getId().toString(),
                tripExpense.getCategoryId().toString(),
                tripExpense.getDate().getMillisecond(),
                tripExpense.getNote().toString(),
                tripExpense.getAmount().toString(),
                tripExpense.getPaymentDetail().isUnpaid(),
                this.getUserPayment(tripExpense),
                this.getUserSpending(tripExpense),
                this.getMembersPayment(tripExpense),
                this.getMemberSpending(tripExpense)
        );
    }

    // region helper method ------------------------------------------------------------------------
    private String getUserPayment(TripExpense tripExpense) {
        Optional<MonetaryAmount> userPayment = tripExpense.getPaymentDetail().getUserPayment();

        if (userPayment.isPresent()) return userPayment.get().toString();
        else return Amount.createZeroAmount().toString();
    }

    private String getUserSpending(TripExpense tripExpense) {
        Optional<MonetaryAmount> userSpending = tripExpense.getSplittingDetail().getUserSpending();

        if (userSpending.isPresent()) return userSpending.get().toString();
        else return Amount.createZeroAmount().toString();
    }

    private Map<String, String> getMembersPayment(TripExpense tripExpense) {
        switch (tripExpense.getPaymentDetail().whoIsPaying().getAnswer()) {
            case USER:
            case UNPAID:
                return new HashMap<>();
            case MEMBER:
            case USER_AND_MEMBER:
                return this.getMembersMap(tripExpense.getPaymentDetail()
                        .getMemberPayment()
                        .orElseThrow(ImpossibleState::new));
            default:
                throw new UnexpectedEnumValue();
        }
    }

    private Map<String, String> getMemberSpending(TripExpense tripExpense) {
        switch (tripExpense.getSplittingDetail().whoIsSpending().getAnswer()) {
            case USER:
                return new HashMap<>();
            case MEMBER:
            case USER_AND_MEMBER:
                return this.getMembersMap(tripExpense.getSplittingDetail()
                        .getMemberSpending()
                        .orElseThrow(ImpossibleState::new));
            default:
                throw new UnexpectedEnumValue();
        }
    }

    private Map<String, String> getMembersMap(Map<ID, MonetaryAmount> amountMap) {
        Map<String, String> map = new HashMap<>();
        amountMap.forEach((id, monetaryAmount) -> map.put(
                id.toString(),
                monetaryAmount.toString()));
        return map;
    }
    // endregion helper method ---------------------------------------------------------------------
}
