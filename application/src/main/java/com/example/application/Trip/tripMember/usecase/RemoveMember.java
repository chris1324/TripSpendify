package com.example.application.Trip.tripMember.usecase;

import com.example.application.Collectible.collBook.CollectibleBookRepository;
import com.example.application.Shared.baseUseCase.useCase.UseCase;
import com.example.application.Trip.tripBook.repository.TripBookRepository;
import com.example.application.Shared.baseUseCase.commandUseCase.CommandUseCase;
import com.example.domain.Collectible.collectibleBook.CollectibleBook;
import com.example.domain.Collectible.collectibleTransaction.CollectibleTransaction;
import com.example.domain.Shared.errorhanding.exception.ImpossibleState;
import com.example.domain.Shared.errorhanding.exception.UnhandledError;
import com.example.domain.Shared.errorhanding.outcome.Outcome;
import com.example.domain.Shared.valueObject.id.ID;
import com.example.domain.Trip.tripBook.TripBook;

import java.util.Optional;

public class RemoveMember extends CommandUseCase<RemoveMember.RequestDTO, RemoveMember.Err> {

    // region Variables and Constructor ------------------------------------------------------------
    private final TripBookRepository mTripBookRepository;
    private final CollectibleBookRepository mCollectibleBookRepository;

    public RemoveMember(TripBookRepository tripBookRepository,
                        CollectibleBookRepository collectibleBookRepository) {
        mTripBookRepository = tripBookRepository;
        mCollectibleBookRepository = collectibleBookRepository;
    }

    // endregion Variables and Constructor ---------------------------------------------------------

    // region RequestDTO  ------------------------------------------------------------------------
    public static class RequestDTO implements UseCase.RequestDTO {
        final String tripBookId;
        final String memberId;

        public RequestDTO(String tripBookId, String memberId) {
            this.tripBookId = tripBookId;
            this.memberId = memberId;
        }
    }

    // endregion RequestDTO  ---------------------------------------------------------------------
    @Override
    protected Outcome<Err> execute(RequestDTO requestDTO) {
        // Prepare
        ID tripBookId = ID.existingId(requestDTO.tripBookId);
        ID memberId = ID.existingId(requestDTO.memberId);

        // CollectibleTransaction
        CollectibleBook collectibleBook = mCollectibleBookRepository.getByTripId(tripBookId);
        Optional<CollectibleTransaction> memberCollTrans = collectibleBook
                .getCollectibleTrans()
                .searchFirst(collectibleTransaction -> collectibleTransaction
                        .getInvolvedMemberId()
                        .equals(memberId));

        if (memberCollTrans.isPresent()) return Outcome.err(Err.HAD_COLL_TRANS);

        //TripExpense
        TripBook tripbook = mTripBookRepository.getById(tripBookId);
        Outcome<TripBook.Err.RemoveMember> tripExpense = tripbook.removeMember(tripBookId);

        if (tripExpense.isErr()) return this.handleTripExpenseErr(tripExpense);

        // Success
        return Outcome.ok();
    }


    // region helper method ------------------------------------------------------------------------
    // endregion helper method ---------------------------------------------------------------------

    // region Error handing ------------------------------------------------------------------------
    public enum Err implements UseCase.ResponseDTO {
        HAD_COLL_TRANS,
        HAD_TRIP_EXPENSE
    }

    private Outcome<Err> handleTripExpenseErr(Outcome<TripBook.Err.RemoveMember> tripExpense) {
        switch (tripExpense.getError().orElseThrow(ImpossibleState::new)) {
            case HAD_TRANSACTION:
                return Outcome.err(Err.HAD_TRIP_EXPENSE);
            default:
                throw new UnhandledError();
        }
    }
    // endregion Error handing ---------------------------------------------------------------------


}
