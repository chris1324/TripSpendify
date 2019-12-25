package com.example.application.Cash.usecase;

import com.example.application.Cash.dto.cashTransactionDTO.CashTransactioDTOMapper;
import com.example.application.Cash.dto.cashTransactionDTO.CashTransactionDTO;
import com.example.application.Cash.repository.CashBookRepository;
import com.example.application.Shared.baseUseCase.queryUseCase.QueryUseCase;
import com.example.application.Shared.baseUseCase.useCase.UseCase;
import com.example.domain.Shared.errorhanding.exception.IntentionallyUnhandledError;
import com.example.domain.Shared.valueObject.id.ID;

import io.reactivex.Observable;

public class FetchCashTrans extends QueryUseCase<FetchCashTrans.RequestDTO, CashTransactionDTO> {

    // region Variables and Constructor ------------------------------------------------------------
    private final CashBookRepository mBookRepository;
    private final CashTransactioDTOMapper mDTOMapper;

    public FetchCashTrans(CashBookRepository bookRepository, CashTransactioDTOMapper DTOMapper) {
        mBookRepository = bookRepository;
        mDTOMapper = DTOMapper;
    }

    // endregion Variables and Constructor ---------------------------------------------------------

    // region RequestDTO  ---------------------------------------------------------------------------
    public static class RequestDTO implements UseCase.RequestDTO {
        public final String mTripBookId;
        public final String mCashTransId;

        public RequestDTO(String tripBookId, String cashTransId) {
            mTripBookId = tripBookId;
            mCashTransId = cashTransId;
        }
    }

    // endregion RequestDTO  -----------------------------------------------------------------------

    @Override
    protected Observable<CashTransactionDTO> execute(RequestDTO requestModel) {
        final ID tripBookId = ID.existingId(requestModel.mTripBookId);
        final ID cashTransId = ID.existingId(requestModel.mCashTransId);

        return mBookRepository.fetchByTripId(tripBookId)
                .map(cashBook -> cashBook.getCashTransactions()
                        .get(cashTransId)
                        .orElseThrow(() -> IntentionallyUnhandledError.maybe("Trip ID or cashTransId ID incorrect")))
                .map(mDTOMapper::mapToDto);
    }

    // ---------------------------------------------------------------------------------------------
}
