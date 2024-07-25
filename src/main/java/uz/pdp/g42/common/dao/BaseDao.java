package uz.pdp.g42.common.dao;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface BaseDao<T> {
    void create(T t) throws IOException;
    T getById(UUID id);
    List<T> list() throws IOException;
}
