package com.example.application.Common.repository;

import com.example.domain.Collectible.collectiblebook.CollectibleBook;
import com.example.domain.Common.sharedvalueobject.id.ID;

public interface CollectibleBookRepository extends Repository<CollectibleBook> {

    CollectibleBook getByTripBkId(ID tripBookId);

    void removeByTripBkID(ID tripBkId);
}
