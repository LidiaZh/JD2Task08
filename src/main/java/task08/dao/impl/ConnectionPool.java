package task08.dao.impl;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import task08.dao.EntityDaoException;

import java.util.ResourceBundle;
import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionPool {
    /**
     * for connection to database
     */
    private final ResourceBundle bundle;
    /**
     * url to database
     */
    private static final String URL = "url";
    /**
     * username to database
     */
    private static final String USER = "user";
    /**
     * password to database
     */
    private static final String PASSWORD = "password";
    /**
     * for database pool connection
     */
    private HikariDataSource dataSource;

    /**
     *
     * @param bundle
     */
    public ConnectionPool(ResourceBundle bundle) {
        this.bundle = bundle;
    }

    /**
     * Get connection with database
     * @return
     * @throws EntityDaoException
     */
    public Connection getConnection() throws EntityDaoException {
        if (dataSource == null) {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(bundle.getString(URL));
            config.setUsername(bundle.getString(USER));
            config.setPassword(bundle.getString(PASSWORD));
            dataSource = new HikariDataSource(config);
        }
        Connection connection;
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            throw new EntityDaoException(e);
        }
        return connection;
    }
}
