package task08.dao;

public class EntityDaoException extends Exception {
    /**
     * EntityDaoException
     */
    public EntityDaoException() {
    }

    /**
     * @param message
     */
    public EntityDaoException(String message) {
        super(message);
    }

    /**
     * @param message
     * @param cause
     */
    public EntityDaoException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param cause
     */
    public EntityDaoException(Throwable cause) {
        super(cause);
    }
}
