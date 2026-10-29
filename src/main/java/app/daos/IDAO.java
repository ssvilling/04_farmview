package app.daos;

import java.util.List;

public interface IDAO<T, I> {
    T create(T t);
    T read(I id);
    List<T> readAll();
    T update(T t, I id);
    void delete(I id);
}
