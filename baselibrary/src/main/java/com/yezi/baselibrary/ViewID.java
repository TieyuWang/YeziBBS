package com.yezi.baselibrary;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author : GW00175635
 * @date : 2020/2/28 17:03
 * desc   : ViewId绑定
 * version: 1.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ViewID {
    int bind();
}
