package org.n3r.sandbox.springmutualref;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceB {
    @Autowired
    ServiceA serviceA;



    public void hello1() {
        System.out.println("ServiceBBBB1111");
    }

    public void hello2() {
        serviceA.hello1();
        System.out.println("ServiceBBBB2222");
    }
}
