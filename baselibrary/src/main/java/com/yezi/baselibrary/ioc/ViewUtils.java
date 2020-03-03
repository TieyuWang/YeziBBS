package com.yezi.baselibrary.ioc;

import android.app.Activity;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author : yezi
 * @date : 2020/2/28 17:09
 * desc   : 绑定View工具类
 * version: 1.0
 */
public class ViewUtils {
    public static void inject(Activity activity){
        inject(new ViewFinder(activity),activity);
    }

    public static void inject(View view){
        inject(new ViewFinder(view),view);
    }

    private static void inject(ViewFinder finder,Object object){
        injectFiled(finder,object);
        injectMethod(finder,object);
    }

    private static void injectMethod(ViewFinder finder, final Object object) {
        Class<?> clazz = object.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        for(final Method method : methods){
            ViewOnClick viewOnClick = method.getAnnotation(ViewOnClick.class);
            if(viewOnClick != null){
                int[] viewIds = viewOnClick.value();
                for(int viewId : viewIds){
                    View view = finder.findViewById(viewId);
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            method.setAccessible(true);
                            try {
                                method.invoke(object,view);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        }
    }

    private static void injectFiled(ViewFinder finder,Object object){
        Class<?> clazz = object.getClass();
        Field [] fields= clazz.getDeclaredFields();
        for(Field field : fields){
            ViewById viewById = field.getAnnotation(ViewById.class);
            if(viewById != null){
                View view = finder.findViewById(viewById.value());
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
