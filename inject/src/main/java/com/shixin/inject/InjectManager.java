package com.shixin.inject;

import android.view.View;

import com.shixin.inject.annotation.InjectEvent;
import com.shixin.inject.annotation.InjectLayout;
import com.shixin.inject.annotation.InjectView;
import com.shixin.inject.proxy.ListenerInvocationHandler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class InjectManager {

    public static void inject(Object obj) {

        injectLayout(obj);
        injectView(obj);
        injectEvent(obj);
    }


    private static void injectView(Object obj) {

        Class<?> clazz  = obj.getClass();
        Field[]  fields = clazz.getFields();
        for (Field field : fields) {
            InjectView injectView = field.getAnnotation(InjectView.class);
            if (injectView != null) {
                try {
                    Method findViewById = clazz.getMethod("findViewById", int.class);
                    int    id           = injectView.value();
                    //得到view
                    View view = ((View) findViewById.invoke(obj, id));
                    //访问私有变量
                    field.setAccessible(true);
                    //设置变量的值
                    field.set(obj, view);
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private static void injectLayout(Object obj) {
        Class<?>     clazz        = obj.getClass();
        InjectLayout injectLayout = clazz.getAnnotation(InjectLayout.class);
        if (injectLayout != null) {
            //  setContentView(R.layout.activity_inject);

            try {
                Method setContentView = clazz.getMethod("setContentView", int.class);
                //静态方法，非静态方法
                //InjectManager.inject()
                // new InjectManager.inject()
                int value = injectLayout.value();

                //非静态方法就需要传入obj，activity对象
                setContentView.invoke(obj, value);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    private static void injectEvent(Object obj) {
        Class<?> clazz   = obj.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            //@override @InjectEvent
            Annotation[] annotations = method.getAnnotations();
            for (Annotation annotation : annotations) {
                Class<? extends Annotation> annotationType = annotation.annotationType();
                InjectEvent                 injectEvent    = annotationType.getAnnotation(InjectEvent.class);
                if (injectEvent != null) {
                    String listenerSetter = injectEvent.listenerSetter();
                    Class  listenerType   = injectEvent.listenerType();
                    String callbackMethod = injectEvent.callbackMethod();

                    ListenerInvocationHandler listenerInvocationHandler
                            = new ListenerInvocationHandler(obj, method);

                    Object proxy = Proxy.newProxyInstance(listenerType.getClassLoader(), new Class[]
                                    {listenerType},
                            listenerInvocationHandler);

                    try {
                        Method value = annotationType.getDeclaredMethod("value");
                        int[]  ids   = (int[]) value.invoke(annotation);
                        for (int id : ids) {
                            Method findViewById = clazz.getMethod("findViewById", int.class);
                            Object view         = findViewById.invoke(obj, id);
                            Method setterMethod = view.getClass().getMethod(listenerSetter, listenerType);
                            setterMethod.invoke(view, proxy);
                        }

                    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }


                }
            }
        }

    }
}
