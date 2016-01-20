package org.n3r.sandbox.jaxb;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class CarTest {
    @Test
    public void getCarAsXml() {
        String registration = "abc123";
        String brand = "Volvo";
        String description = "Sedan<xx.yy@gmail.com>";

        Car car = new Car(registration, brand, description);
        String xml = XmlUtil.marshal(car);
        System.out.println((xml));

        Car car2 = XmlUtil.unmarshal(xml, Car.class);
        assertThat(car2, is(equalTo(car)));


        String xpathExpression = "/car/@registration";
        String actual = XmlUtil.extractValue(xml, xpathExpression);
        assertThat(actual, is(registration));

        xpathExpression = "/car/brand";
        actual = XmlUtil.extractValue(xml, xpathExpression);
        assertThat(actual, is(brand));

        xpathExpression = "/car/description";
        actual = XmlUtil.extractValue(xml, xpathExpression);
        assertThat(actual, is(description));
    }
}
