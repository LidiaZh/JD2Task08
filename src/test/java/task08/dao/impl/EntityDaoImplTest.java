package task08.dao.impl;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import task08.dao.EntityDao;
import task08.dao.EntityDaoException;
import task08.entity.Car;

import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EntityDaoImplTest extends Assert {
    public static final String DATABASE_H2 = "databaseH2";
    public static final String URL = "jdbc:h2:mem:mydb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;TRACE_LEVEL_SYSTEM_OUT=3";
    public static final String USER = "sa";
    public static final String PASSWORD = "";
    public static final String INSERT_STATEMENT = "INSERT INTO %s (%s) VALUES (%s)";
    private static ConnectionPool connectionPool;
    private static EntityDao dao;
    private static List<List<Object>> listOfCars = new LinkedList<>();
    private static Car car1;
    private static Car car2;
    private static Car car3;
    private static Car car4;
    private static String carTest1;
    private static String carTest2;
    private static String carTest3;
    private static String carTest4;

    @BeforeClass
    public static void setUp() throws Exception {

        ResourceBundle bundleH2 = ResourceBundle.getBundle(DATABASE_H2);
        connectionPool = new ConnectionPool(bundleH2);
        assertTrue(connectionPool.getConnection() != null);
        String query = "create table car(\n" +
                "    id int auto_increment primary key,\n" +
                "    identifier int,\n" +
                "    name varchar(25),\n" +
                "    color varchar(25),\n" +
                "price dec(10)\n" +
                ");";
        Statement statement = connectionPool.getConnection().createStatement();
        int i = statement.executeUpdate(query);
        dao = new EntityDaoImpl(Car.class, connectionPool);
        assertTrue(dao != null);

        car1 = new Car(1010, "fiat", "black", 4500L);
        car2 = new Car(2020, "opel", "green", 5000L);
        car3 = new Car(3030, "volvo", "blue", 10000L);
        car4 = new Car(4040, "toyota", "white", 1500L);
        carTest1 = "[1, 1010, fiat, black, 4500]";
        carTest2 = "[2, 2020, opel, green, 5000]";
        carTest3 = "[3, 3030, volvo, blue, 10000]";
        carTest4 = "[2, 4040, toyota, white, 1500]";
    }

    @Test
    public void A_insert() throws EntityDaoException {

        dao.insert(car1);
        dao.insert(car2);
        dao.insert(car3);

        assertEquals(carTest1, dao.select().get(0).toString());
        assertEquals(carTest2, dao.select().get(1).toString());
        assertEquals(carTest3, dao.select().get(2).toString());
        assertNotEquals(carTest4, dao.select().get(2).toString());
    }

    @Test
    public void B_testUpdate() throws EntityDaoException {
        dao.update(2L, car4);
        assertEquals(carTest4, dao.select().get(1).toString());
    }

    @Test
    public void C_select() throws EntityDaoException {
        dao.select();
        assertEquals(carTest1, dao.select().get(0).toString());
        assertEquals(carTest4, dao.select().get(1).toString());
        assertEquals(carTest3, dao.select().get(2).toString());
    }

    @Test
    public void testDelete() throws EntityDaoException {
        dao.delete(3L);
        try {
            dao.select().get(2);
        } catch (Exception ex) {
            System.out.println(ex);
            System.out.println("Object was deleted");
        }
    }

}