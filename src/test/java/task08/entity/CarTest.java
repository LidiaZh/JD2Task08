package task08.entity;

import org.junit.Assert;
import org.junit.Test;

public class CarTest extends Assert {
    @Test
    public void testToStringCar() {
        Car car = new Car(1111, "lada", "yellow", 2000L);
        String str = "Car{identifier=1111, name='lada', color='yellow', price=2000}";
        assertEquals(str, car.toString());
    }
}
