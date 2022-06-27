/**
 * Create Class Car
 */
package task08.entity;

import java.util.Objects;

@MyTable(name = "car")
public class Car {
    @MyColumn(name = "identifier")
    private Integer identifier;
    @MyColumn(name = "name")
    private String name;
    @MyColumn(name = "color")
    private String color;
    @MyColumn(name = "price")
    private Long price;

    /**
     * Car constructor
     * @param identifier
     * @param name
     * @param color
     * @param price
     */
    public Car(Integer identifier, String name, String color, Long price) {
        this.identifier = identifier;
        this.name = name;
        this.color = color;
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return Objects.equals(identifier, car.identifier)
                && Objects.equals(price, car.price)
                && Objects.equals(name, car.name)
                && Objects.equals(color, car.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier, name, color, price);
    }

    @Override
    public String toString() {
        return "Car{" +
                "identifier=" + identifier
                + ", name='" + name + '\''
                + ", color='" + color + '\''
                + ", price=" + price + '}';
    }
}
