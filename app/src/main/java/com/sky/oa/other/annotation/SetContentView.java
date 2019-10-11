package com.sky.oa.other.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author sky QQ:1136096189
 * @Description: TODO 抄也要抄一遍
 * @date 15/12/23 下午8:36
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SetContentView {
    int value();
}
