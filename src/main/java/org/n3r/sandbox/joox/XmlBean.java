package org.n3r.sandbox.joox;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = "bean")
public class XmlBean {
    private String name;
    private String addr;
    private String firstName;
    private int age;
    private boolean moneyful;

    @XmlElement(name = "fullName")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @XmlTransient
    public boolean isMoneyful() {
        return moneyful;
    }

    public void setMoneyful(boolean moneyful) {
        this.moneyful = moneyful;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        XmlBean xmlBean = (XmlBean) o;

        if (age != xmlBean.age) return false;
        if (moneyful != xmlBean.moneyful) return false;
        if (name != null ? !name.equals(xmlBean.name) : xmlBean.name != null) return false;
        if (addr != null ? !addr.equals(xmlBean.addr) : xmlBean.addr != null) return false;
        return !(firstName != null ? !firstName.equals(xmlBean.firstName) : xmlBean.firstName != null);

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (addr != null ? addr.hashCode() : 0);
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + age;
        result = 31 * result + (moneyful ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "XmlBean{" +
                "name='" + name + '\'' +
                ", addr='" + addr + '\'' +
                ", firstName='" + firstName + '\'' +
                ", age=" + age +
                ", moneyful=" + moneyful +
                '}';
    }
}
