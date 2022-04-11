package task08.dao;

import java.util.List;

public interface EntityDao {
    /**
     * @return
     * @throws EntityDaoException
     */
    List<List<Object>> select() throws EntityDaoException;

    /**
     * @param id
     * @param entity
     * @throws EntityDaoException
     */
    void update(Long id, Object entity) throws EntityDaoException;

    /**
     * @param id
     * @throws EntityDaoException
     */
    void delete(Long id) throws EntityDaoException;

    /**
     * @param entity
     * @return
     * @throws EntityDaoException
     */
    boolean insert(Object entity) throws EntityDaoException;
}
