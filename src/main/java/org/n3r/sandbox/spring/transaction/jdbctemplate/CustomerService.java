package org.n3r.sandbox.spring.transaction.jdbctemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class CustomerService {
    @Autowired
    DataSource dataSource;

    public void create(Customer customer) {
        String queryCustomer = "insert into Customer (id, name) values (?,?)";
        String queryAddress = "insert into Address (id, address,country) values (?,?,?)";

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        jdbcTemplate.update(queryCustomer, new Object[] { customer.getId(),
                customer.getName() });
        System.out.println("Inserted into Customer Table Successfully");
        jdbcTemplate.update(queryAddress, new Object[] { customer.getId(),
                customer.getAddress().getAddress(),
                customer.getAddress().getCountry() });
        System.out.println("Inserted into Address Table Successfully");
    }
}
