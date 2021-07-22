package interfaces;

import java.util.List;

public interface DAO<T> {
    public void create(T model);

    public void update(T model);

    T findBy(String field, String value);

    List<T> selectAll();
}
