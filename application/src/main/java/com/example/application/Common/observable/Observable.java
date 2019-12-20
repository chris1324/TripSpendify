package com.example.application.Common.observable;

import java.util.Set;

public interface Observable<L> {

    void registerListener(L listener);

    void unregisterListener(L listener);

    Set<L> getListeners();
}
