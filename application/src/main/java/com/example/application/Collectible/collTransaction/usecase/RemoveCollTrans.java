package com.example.application.Collectible.collTransaction.usecase;

import com.example.application.Collectible.collBook.CollectibleBookRepository;
import com.example.application.Shared.baseUseCase.commandUseCase.CommandUseCase;
import com.example.application.Shared.baseUseCase.useCase.UseCase;
import com.example.domain.Collectible.collectibleBook.CollectibleBook;
import com.example.domain.Collectible.collectibleBook.CollectibleTransactionService;
import com.example.domain.Shared.errorhanding.outcome.Outcome;
import com.example.domain.Shared.valueObject.id.ID;

public class RemoveCollTrans extends CommandUseCase<RemoveCollTrans.RequestDTO, UseCase.Void> {

    // region Variables and Constructor ------------------------------------------------------------
    private final CollectibleBookRepository mCollectibleBookRepository;
    private final CollectibleTransactionService mCollectibleTransactionService;

    public RemoveCollTrans(CollectibleBookRepository collectibleBookRepository,
                           CollectibleTransactionService collectibleTransactionService) {
        mCollectibleBookRepository = collectibleBookRepository;
        mCollectibleTransactionService = collectibleTransactionService;
    }

    // endregion Variables and Constructor ---------------------------------------------------------

    // region RequestDTO  ------------------------------------------------------------------------
    public static class RequestDTO implements UseCase.RequestDTO {
        final String tripBookId;
        final String collTransId;

        public RequestDTO(String tripBookId, String collTransId) {
            this.tripBookId = tripBookId;
            this.collTransId = collTransId;
        }
    }
    // endregion RequestDTO  ---------------------------------------------------------------------

    @Override
    protected Outcome<Void> execute(RequestDTO requestDTO) {
        CollectibleBook collectibleBook = mCollectibleBookRepository.getByTripId(ID.existingId(requestDTO.tripBookId));
        mCollectibleTransactionService.removeCollTrans(collectibleBook, ID.existingId(requestDTO.collTransId));
        return Outcome.ok();
    }

}
