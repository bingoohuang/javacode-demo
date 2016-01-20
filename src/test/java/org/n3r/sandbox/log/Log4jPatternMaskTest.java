package org.n3r.sandbox.log;

import org.junit.Test;
import org.apache.log4j.Logger;

public class Log4jPatternMaskTest {
    Logger logger = Logger.getLogger(LogbackPatternMaskTest.class);

    @Test
    public void test() {
        // 18ID：123456++++++78123X, 15ID：123456++++++123
        logger.info("18ID：12345612345678123X, 15ID：123456123456123");
        // EmpNo：6***6
        logger.info("EmpNo：60476");
    }
}
