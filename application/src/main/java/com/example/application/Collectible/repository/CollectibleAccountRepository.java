package com.example.application.Collectible.repository;

import com.example.application.Shared.repository.Repository;
import com.example.domain.Shared.valueObject.id.ID;
import com.example.domain.Collectible.collectibleAccount.CollectibleAccount;

import io.reactivex.Observable;

public interface CollectibleAccountRepository extends Repository {
    Observable<CollectibleAccount> fetchByTripId(ID tripId);
}
