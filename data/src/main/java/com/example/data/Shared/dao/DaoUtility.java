package com.example.data.Shared.dao;

import java.util.List;

public interface DaoUtility {

    void delete(String id);

    void delete(List<String> ids);

    boolean exist(String id);
}
