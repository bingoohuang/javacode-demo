package org.n3r.sandbox.spring.transaction.jdbctemplate;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.security.SecureRandom;

public class CustomerMain {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring/springCustomer.xml");
        CustomerService service = ctx.getBean(CustomerService.class);

        Customer cust = createDummyCustomer();
        service.create(cust);

        ctx.close();
    }

    private static Customer createDummyCustomer() {
        int id = new SecureRandom().nextInt(10000);
        Customer customer = new Customer();
        customer.setId(id);
        customer.setName("Pankaj");
        Address address = new Address();
        address.setId(id);
        address.setCountry("India");
        // setting value more than 20 chars, so that SQLException occurs
        address.setAddress("San Jose, CA 95129");
        customer.setAddress(address);
        return customer;
    }
}

