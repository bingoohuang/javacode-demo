package org.n3r.sandbox.lang;

import com.alibaba.fastjson.JSON;

import java.lang.reflect.ParameterizedType;

// 这里使用抽象类，防止被直接实例化
abstract class ReqMsg<T> {
    private String content;

    Class<T> persistentClass = (Class<T>) ((ParameterizedType) getClass()
            .getGenericSuperclass()).getActualTypeArguments()[0];

    public ReqMsg(String content) {
        this.content = content;
    }

    public T getContent() {
        return JSON.parseObject(content, persistentClass);
    }
}

class Bean {
    String name;

    public Bean() {
    }

    public Bean(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Bean{" +
                "name='" + name + '\'' +
                '}';
    }
}

public class GenericType {
    public static void main(String[] args) {
        Bean bingoo = new Bean("bingoo");
        String json = JSON.toJSONString(bingoo);
        System.out.println(json); // {"name":"bingoo"}

        ReqMsg<Bean> beanReqMsg = new ReqMsg<Bean>(json) {}; // 注意：这里使用匿名子类

        Bean content = beanReqMsg.getContent();
        System.out.println(content); // Bean{name='bingoo'}
    }
}
