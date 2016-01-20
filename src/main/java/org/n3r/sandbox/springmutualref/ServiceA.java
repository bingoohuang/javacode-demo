package org.n3r.sandbox.springmutualref;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceA {
    @Autowired
    ServiceB serviceB;

    public void hello1() {
        System.out.println("ServiceAAAA1111");
    }

    public void hello2() {
        serviceB.hello1();
        System.out.println("ServiceAAAA2222");
    }
}
