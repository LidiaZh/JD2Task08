/**
 * Main class to work with project
 */
package task08;

import task08.dao.EntityDao;
import task08.dao.EntityDaoException;
import task08.dao.impl.ConnectionPool;
import task08.dao.impl.EntityDaoImpl;
import task08.dao.impl.PrintUtils;
import task08.entity.Car;

import java.util.ResourceBundle;

class App {
    /**
     * Name of database file
     */
    public static final String DATABASE_MY_SQL = "database";
    /**
     * Id for updating data in table
     */
    public static final long ID_FOR_UPDATE = 4L;
    /**
     * Id for deleting data in table
     */
    public static final long ID_FOR_DELETE = 2L;

    /**
     *
     * @param args
     * @throws EntityDaoException
     */
    public static void main(String[] args) throws EntityDaoException {
        ResourceBundle bundleMySql = ResourceBundle.getBundle(DATABASE_MY_SQL);
        ConnectionPool connectionPool = new ConnectionPool(bundleMySql);
        EntityDao dao = new EntityDaoImpl(Car.class, connectionPool);

        PrintUtils.printList("Original DB:", dao.select());

        Car car4 = new Car(4000, "volvo", "red", 20000L);
        System.out.println("\nCreate Car(4000, \"volvo\", \"red\",20000)");

        dao.update(ID_FOR_UPDATE, car4);
        PrintUtils.printList("\nUpdated position #" + ID_FOR_UPDATE
                        + " by Car (4000...):", dao.select());

        dao.delete(ID_FOR_DELETE);
        PrintUtils.printList("\nDeleted position #" + ID_FOR_DELETE + " :",
                dao.select());

        dao.insert(new Car(7887, "lada", "black", 2100L));
        PrintUtils.printList("\nInserted Car(7887, \"lada\", \"black\",2100):",
                dao.select());
    }
}
