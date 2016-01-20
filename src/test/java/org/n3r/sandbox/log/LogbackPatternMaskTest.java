package org.n3r.sandbox.log;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogbackPatternMaskTest {
    Logger logger = LoggerFactory.getLogger(LogbackPatternMaskTest.class);

    @Test
    public void test() {
        // 18ID：123456++++++78123X, 15ID：123456++++++123
        logger.info("18ID：12345612345678123X, 15ID：123456123456123");
        // EmpNo：6***6
        logger.info("EmpNo：60476");
    }
}
