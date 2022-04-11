package task08.dao.impl;

import java.util.List;

public class PrintUtils {
    /**
     * for printing meanings of table
     * @param message
     * @param list
     */
    public static void printList(String message, List<List<Object>> list) {
        System.out.println(message);
        for (List<Object> l : list) {
            for (Object o : l) {
                System.out.printf("%-10s ", o.toString());
            }
            System.out.println();
        }
    }
}
