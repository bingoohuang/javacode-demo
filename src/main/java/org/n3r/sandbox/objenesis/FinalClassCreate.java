package org.n3r.sandbox.objenesis;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.joor.Reflect;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

public class FinalClassCreate {
    @ToString @AllArgsConstructor
    public static class FinalBean {
        @Getter private final String name;
    }

    public static void main(String[] args) {
        Objenesis objenesis = new ObjenesisStd();
        FinalBean o = objenesis.newInstance(FinalBean.class);  // 能成功创建
        Reflect.on(o).set("name", "bingoo");
        System.out.println(o);

        // 异常：com.alibaba.fastjson.JSONException: default constructor not found. class org.n3r.sandbox.objenesis.FinalClassCreate$FinalBean
        FinalBean finalBean = JSON.parseObject("{name:\"dingoo\"}", FinalBean.class);
        System.out.println(finalBean);

    }
}
