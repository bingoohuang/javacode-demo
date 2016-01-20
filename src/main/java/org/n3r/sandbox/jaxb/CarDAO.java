package org.n3r.sandbox.jaxb;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.LinkedList;
import java.util.List;

@XmlRootElement(name = "cars")
public class CarDAO {
    @XmlAnyElement
    public List<Car> cars;

    public CarDAO() {
        cars = new LinkedList<Car>();
    }

    public void addCar(Car car) {
        cars.add(car);
    }
}
