package org.n3r.sandbox.el;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import ognl.Ognl;
import ognl.OgnlException;
import org.junit.Test;

import java.util.Map;

import static com.google.common.collect.ImmutableMap.of;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class OgnlTest {
    @Test
    public void oneCharStringEquals() {
        try {
            Object value = Ognl.getValue("attr.tag == 1", of("attr", of("tag", "1")));
            assertThat(value instanceof Boolean, is(true));
            assertThat((boolean)value, is(true));

            value = Ognl.getValue("attr.tag == \"1\"", of("attr", of("tag", "1")));
            assertThat(value instanceof Boolean, is(true));
            assertThat((boolean)value, is(true));

            value = Ognl.getValue("attr.tag == '1'", of("attr", of("tag", "1")));
            // WARN: '1' is treated as a single character
            assertThat(value instanceof Boolean, is(true));
            assertThat((boolean)value, is(false));

        } catch (OgnlException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void selection() {
        String expr = "goodsAttrs.{?#this.attrType==\"1\"}[0].attrId";
        Map map = ImmutableMap.of("goodsAttrs", Lists.newArrayList(of("attrType", "1", "attrId", "aaaa")));
        try {
            Object value = Ognl.getValue(expr, map);
            assertThat((String)value, equalTo("aaaa"));
        } catch (OgnlException e) {
            e.printStackTrace();
        }
    }
}
