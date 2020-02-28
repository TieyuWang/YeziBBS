package com.yezi.baselibrary;

import android.app.Activity;
import android.view.View;

import java.lang.reflect.Field;

/**
 * @author : GW00175635
 * @date : 2020/2/28 17:09
 * desc   : 绑定View工具类
 * version: 1.0
 */
public class ViewUtils {
    public static void inject(Activity activity){
        bindView(new ViewFinder(activity),activity);
    }

    private static void bindView(ViewFinder finder,Object object){
        Class<?> clazz = object.getClass();
        Field [] fields= clazz.getDeclaredFields();
        for(Field field : fields){
            ViewID viewId = field.getAnnotation(ViewID.class);
            if(viewId != null){
                View view = finder.findViewById(viewId.bind());
                field.setAccessible(true);
                try {
                    field.set(object,view);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
