package com.example.application.Cash.cashTransction.usecase;

import com.example.application.Cash.cashBook.CashBookRepository;
import com.example.application.Shared.baseUseCase.commandUseCase.CommandUseCase;
import com.example.application.Shared.baseUseCase.useCase.UseCase;
import com.example.domain.Cash.cashBook.CashBook;
import com.example.domain.Cash.cashBook.CashTransactionService;
import com.example.domain.Shared.errorhanding.outcome.Outcome;
import com.example.domain.Shared.valueObject.id.ID;

public class RemoveCashTrans extends CommandUseCase<RemoveCashTrans.RequestDTO, UseCase.Void> {

    // region Variables and Constructor ------------------------------------------------------------
    private final CashBookRepository mCashBookRepository;
    private final CashTransactionService mCashTransactionService;

    public RemoveCashTrans(CashBookRepository cashBookRepository, CashTransactionService cashTransactionService) {
        mCashBookRepository = cashBookRepository;
        mCashTransactionService = cashTransactionService;
    }

    // endregion Variables and Constructor ---------------------------------------------------------

    // region RequestDTO  ------------------------------------------------------------------------
    // endregion RequestDTO  ---------------------------------------------------------------------
    public static class RequestDTO implements UseCase.RequestDTO {
        final String tripBookId;
        final String cashTransId;

        public RequestDTO(String tripBookId, String cashTransId) {
            this.tripBookId = tripBookId;
            this.cashTransId = cashTransId;
        }
    }

    @Override
    protected Outcome<Void> execute(RequestDTO requestDTO) {
        CashBook cashBook = mCashBookRepository.getByTripId(ID.existingId(requestDTO.tripBookId));
        mCashTransactionService.removeCashTrans(cashBook,ID.existingId(requestDTO.cashTransId));
        return Outcome.ok();
    }

    // region helper method ------------------------------------------------------------------------
    // endregion helper method ---------------------------------------------------------------------

    // region Error handing ------------------------------------------------------------------------
    public enum Err {}
    // endregion Error handing ---------------------------------------------------------------------
}
