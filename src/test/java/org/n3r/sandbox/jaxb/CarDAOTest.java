package org.n3r.sandbox.jaxb;


import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class CarDAOTest {

    @Test
    public void getCarsAsXml() {
        String firstCarRegistration = "abc123";
        String secondCarRegistration = "123abc";

        Car car = new Car(firstCarRegistration, "Volvo", "Sedan");

        CarDAO carDAO = new CarDAO();
        carDAO.addCar(car);

        car = new Car(secondCarRegistration, "Opel", "Truck");
        carDAO.addCar(car);

        XmlUtil xmlUtil = new XmlUtil();
        String xml = xmlUtil.marshal(carDAO, Car.class, CarDAO.class);
        System.out.println(xml);

        String xpathExpression = "/cars/car/@registration";
        String actual = xmlUtil.extractValue(xml, xpathExpression);
        assertThat(actual, is(firstCarRegistration));
    }
}
