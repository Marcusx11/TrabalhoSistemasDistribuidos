package interfaces;

import java.util.List;

public interface DAO<T> {
    void create(T model);

    void update(T model);

    T findBy(String field, String value);

    List<T> selectBy(String field, String value);

    List<T> selectAll();
}
