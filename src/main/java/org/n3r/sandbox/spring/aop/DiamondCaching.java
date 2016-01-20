package org.n3r.sandbox.spring.aop;

import java.lang.annotation.*;

/**
 * 基于diamond的标注方法返回缓存。
 * 缓存的key为arg1:arg2:arg3
 * 当diamond中的相同group,dataId的值发生变化时，值内容以换行分割后作为多个key，去除缓存中对应的值（缓存部分失效）。
 */
@Inherited
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DiamondCaching {
    String group() default ""; // method host class name like x.y.c.ZabService

    String dataId() default ""; // method name like addUser
}