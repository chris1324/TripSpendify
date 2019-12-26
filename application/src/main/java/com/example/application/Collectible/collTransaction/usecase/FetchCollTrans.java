package com.example.application.Collectible.collTransaction.usecase;

import com.example.application.Collectible.collBook.CollectibleBookRepository;
import com.example.application.Collectible.collTransaction.dto.CollTransactionDTO;
import com.example.application.Collectible.collTransaction.dto.CollTransactionDTOMapper;
import com.example.application.Shared.baseUseCase.queryUseCase.QueryUseCase;
import com.example.application.Shared.baseUseCase.useCase.UseCase;
import com.example.domain.Shared.errorhanding.exception.IntentionallyUnhandledError;
import com.example.domain.Shared.valueObject.id.ID;

import io.reactivex.Observable;

public class FetchCollTrans extends QueryUseCase<FetchCollTrans.RequestDTO, CollTransactionDTO> {

    // region Variables and Constructor ------------------------------------------------------------
    private final CollectibleBookRepository mBookRepository;
    private final CollTransactionDTOMapper mDTOMapper;

    public FetchCollTrans(CollectibleBookRepository bookRepository, CollTransactionDTOMapper DTOMapper) {
        mBookRepository = bookRepository;
        mDTOMapper = DTOMapper;
    }

    // endregion Variables and Constructor ---------------------------------------------------------

    // region RequestDTO  ---------------------------------------------------------------------------
    public static class RequestDTO implements UseCase.RequestDTO {
        public final String mTripBookId;
        public final String mCollTransId;

        public RequestDTO(String tripBookId, String collTransId) {
            mTripBookId = tripBookId;
            mCollTransId = collTransId;
        }
    }
    // endregion RequestDTO  -----------------------------------------------------------------------

    @Override
    protected Observable<CollTransactionDTO> execute(RequestDTO requestModel) {
        final ID tripBookId = ID.existingId(requestModel.mTripBookId);
        final ID collTransId = ID.existingId(requestModel.mCollTransId);

        return mBookRepository.fetchByTripId(tripBookId)
                .map(collectibleBook -> collectibleBook.getCollectibleTrans()
                        .get(collTransId)
                        .orElseThrow(() -> IntentionallyUnhandledError.maybe("Trip ID or CollTrans ID incorrect")))
                .map(mDTOMapper::mapToDto);
    }

    // ---------------------------------------------------------------------------------------------
}
