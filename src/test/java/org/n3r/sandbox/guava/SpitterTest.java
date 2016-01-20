package org.n3r.sandbox.guava;

import com.google.common.base.Splitter;
import org.junit.Test;

import java.util.Map;

public class SpitterTest {
    @Test
    public void test() {
        String str = "{x:214,y:445,s:1856x611,d:1380186862,t:b}";
        str = str.substring(1, str.length() - 2);
        Splitter.MapSplitter splitter = Splitter.on(',').omitEmptyStrings().trimResults().withKeyValueSeparator(':');
        Map<String,String> split = splitter.split(str);
        System.out.println(split);
    }
}
