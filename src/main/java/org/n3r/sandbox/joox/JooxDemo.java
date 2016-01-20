package org.n3r.sandbox.joox;

import static org.joox.JOOX.$;

public class JooxDemo {
    public static void main(String[] args) {
        XmlBean xmlBean = new XmlBean();
        xmlBean.setAddr("nanjing");
        xmlBean.setAge(101);
        xmlBean.setFirstName("bingoo");
        xmlBean.setMoneyful(true);
        xmlBean.setName("黄进兵<bingoo.huang@gmail.com>");


        String xml = $(xmlBean).toString();
        System.out.println(xml);

        // <bean><addr>nanjing</addr><age>101</age><firstName>bingoo</firstName><fullName>黄进兵&lt;bingoo.huang@gmail.com&gt;</fullName></bean>

        XmlBean xmlBean1 = $(xml).unmarshalOne(XmlBean.class);
        System.out.println(xmlBean1);
    }
}
