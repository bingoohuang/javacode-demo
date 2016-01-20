package org.n3r.sandbox.spring.transaction.mybatis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class TestingService {
    @Autowired
    TestingMapper testingMapper;

    public void insert() {
        testingMapper.insert(new Testing("" + new Random().nextInt(), "" + new Random().nextInt()));
//        testingMapper.insert(new Testing()); // this will throw an exception
    }
}
