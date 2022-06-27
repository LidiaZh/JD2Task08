package task08.dao.impl;

import task08.dao.EntityDao;
import task08.dao.EntityDaoException;
import task08.entity.MyColumn;
import task08.entity.MyTable;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class EntityDaoImpl implements EntityDao {
    private static final String SELECT_ALL_FROM_TABLE = "SELECT * FROM %s";
    //    private static final String SELECT_PARAM_FROM_TABLE =
    //    "SELECT * FROM %s WHERE name = %s";
    private static final String DELETE_STATEMENT =
            "DELETE FROM %s WHERE id = %d";
    private static final String INSERT_STATEMENT =
            "INSERT INTO %s (%s) VALUES (%s)";
    private static final String UPDATE_STATEMENT =
            "UPDATE %s SET %s WHERE id = %d";
    private static final String SINGLE_QUOTE_SIGN = "'";
    private static final String COMMA_SIGN = ",";
    private static final String EQUAL_SIGN = "=";
    private final String tableName;
    private final String[] tableColumnNames;
    private final String[] classFieldNames;
    private final ConnectionPool connectionPool;

    /**
     *
     * @param aClass
     * @param connectionPool
     * @throws EntityDaoException
     */
    public EntityDaoImpl(Class aClass, ConnectionPool connectionPool)
            throws EntityDaoException {

        this.connectionPool = connectionPool;

        // check class for ability to persistence
        if (aClass.isAnnotationPresent(MyTable.class)) {
            tableName = ((MyTable) aClass.getAnnotation(MyTable.class)).name();
        } else {
            throw new EntityDaoException("No 'MyTable' annotation");
        }

        // looking for annotated fields
        Field[] fields = aClass.getDeclaredFields();
        int fieldCount = 0;
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(MyColumn.class)) {
                fieldCount++;
            }
            field.setAccessible(false);
        }

        tableColumnNames = new String[fieldCount];
        classFieldNames = new String[fieldCount];
        Class[] classFieldTypes = new Class[fieldCount];
        int index = 0;
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(MyColumn.class)) {
                tableColumnNames[index] = (field.getAnnotation(MyColumn.class))
                        .name();
                classFieldTypes[index] = field.getType();
                classFieldNames[index++] = field.getName();

            }
            field.setAccessible(false);
        }
    }

    @Override
    public List<List<Object>> select() throws EntityDaoException {
        List<List<Object>> list = new LinkedList<>();
        try (
                Connection connection = connectionPool.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet =
                        statement.executeQuery( /* SELECT * FROM %s */
                                String.format(SELECT_ALL_FROM_TABLE, tableName))
        ) {
            while (resultSet.next()) {
                List<Object> listOfFields = new LinkedList<>();
                for (int i = 0; i < classFieldNames.length + 1; i++) {
                    listOfFields.add(resultSet.getObject(i + 1));
                }
                list.add(listOfFields);
            }
        } catch (SQLException e) {
            throw new EntityDaoException(e);
        }
        return list;
    }

    @Override
    public void update(Long id, Object entity) throws EntityDaoException {
        int updatedCount;
        String[] params = getObjectParam(entity);
        try (
                Connection connection = connectionPool.getConnection();
                Statement statement = connection.createStatement()
        ) {
            updatedCount =
                    statement.executeUpdate(/* UPDATE %s SET %s WHERE id = %d */
                            String.format(UPDATE_STATEMENT,
                                    tableName,
                                    IntStream.range(0, tableColumnNames.length)
                                            .mapToObj(i -> tableColumnNames[i]
                                                    .concat(EQUAL_SIGN)
                                                    .concat(params[i]))
                                            .collect(Collectors
                                                    .joining(COMMA_SIGN)),
                                    id
                            ));
        } catch (SQLException e) {
            throw new EntityDaoException(e);
        }

        if (updatedCount != 1) {
            throw new EntityDaoException("Updating transaction failed " +
                    "(updatedCount != 1)");
        }
    }

    @Override
    public void delete(Long id) throws EntityDaoException {
        int deletedCount;
        try (
                Connection connection = connectionPool.getConnection();
                Statement statement = connection.createStatement()
        ) {
            deletedCount =
                    statement.executeUpdate( /* DELETE FROM %s WHERE id = %d*/
                            String.format(DELETE_STATEMENT,
                                    tableName,
                                    id));
        } catch (SQLException e) {
            throw new EntityDaoException(e);
        }
        if (deletedCount != 1) {
            throw new EntityDaoException("Deletion transaction failed " +
                    "(deletedCount != 1)");
        }
    }

    @Override
    public boolean insert(Object entity) throws EntityDaoException {
        int insertedCount;
        String[] params = getObjectParam(entity);

        try (
                Connection connection = connectionPool.getConnection();
                Statement statement = connection.createStatement()
        ) {
            insertedCount =
                    statement.executeUpdate( /*INSERT INTO %s (%s) VALUES (%s)*/
                            String.format(INSERT_STATEMENT,
                                    tableName,
                                    String.join(COMMA_SIGN, tableColumnNames),
                                    String.join(COMMA_SIGN, params)
                            ));

        } catch (SQLException e) {
            throw new EntityDaoException(e);
        }
        if (insertedCount != 1) {
            throw new EntityDaoException("Insertion transaction failed " +
                    "(insertedCount != 1)");
        }
        return false;
    }

    private String[] getObjectParam(Object entity) throws EntityDaoException {
        String[] param = new String[classFieldNames.length];
        try {
            for (int i = 0; i < classFieldNames.length; i++) {
                Field field = entity.getClass()
                        .getDeclaredField(classFieldNames[i]);
                field.setAccessible(true);
                Object fieldValue = field.get(entity);
                if (fieldValue instanceof CharSequence) {
                    fieldValue = SINGLE_QUOTE_SIGN
                            .concat(((CharSequence) fieldValue)
                                    .toString())
                            .concat(SINGLE_QUOTE_SIGN);
                }
                param[i] = fieldValue.toString();
                field.setAccessible(false);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new EntityDaoException(e);
        }
        return param;
    }
}
