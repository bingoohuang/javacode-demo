package org.n3r.sandbox.spring.transaction.mybatis;

import java.io.Serializable;

/*
CREATE TABLE `testing` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(30) NOT NULL DEFAULT '',
  `address` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`Id`),
  UNIQUE KEY `ix` (`name`)
)
 */
public class Testing implements Serializable  {

    private Integer id;
    private String name;
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "testing{" + "id=" + id + ", name=" + name + ", address=" + address + '}';
    }

    public Testing() {
    }

    public Testing(String name, String address) {
        this.name = name;
        this.address = address;
    }
}
