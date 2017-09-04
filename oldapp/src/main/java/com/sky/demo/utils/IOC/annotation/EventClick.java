package com.sky.demo.utils.IOC.annotation;

import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author sky QQ:1136096189
 * @Description: TODO
 * @date 15/12/23 下午8:40
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventClick {
    /**
     * 控件id的集合
     * @return
     */
    int[] value();

    /**
     * 控件的父控件的id的集合
     * @return
     */
    int[] parentId() default 0;

    /**
     * 事件的listener，默认为点击事件
     * @return
     */
    Class<?> type() default View.OnClickListener.class;

    /**
     * 事件的setter方法名, 默认为set+type#simpleName.
     *
     * @return
     */
    String setter() default "";
    /**
     * 如果type的接口类型提供多个方法, 需要使用此参数指定方法名.
     *
     * @return
     */
    String method() default "";

}
