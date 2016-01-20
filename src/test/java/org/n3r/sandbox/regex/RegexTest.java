package org.n3r.sandbox.regex;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class RegexTest {
    @Test
    public void testFindAndAppendTail() {
        Pattern p = Pattern.compile("cat");
        Matcher m = p.matcher("one cat two cats in the yard");
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(sb, "abc");
        }
        m.appendTail(sb);

        assertThat(sb.toString(), is("one abc two abcs in the yard"));
    }

    @Test
    public void testFindAndAppendTail2() {
        Pattern p = Pattern.compile("cat");
        Matcher m = p.matcher("one cat two cats in the yard");
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(sb, "$0abc");
        }
        m.appendTail(sb);

        assertThat(sb.toString(), is("one catabc two catabcs in the yard"));
    }
}
