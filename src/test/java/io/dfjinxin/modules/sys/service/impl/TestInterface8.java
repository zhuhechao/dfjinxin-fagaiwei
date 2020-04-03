package io.dfjinxin.modules.sys.service.impl;

/**
 * @Desc:
 * @Author: z.h.c
 * @Date: 2020/3/26 11:12
 * @Version: 1.0
 */
public interface TestInterface8 {
    /**
     * test
     */
    void test();


    /**
     * Java8中允许接口中定义非抽象方法 前提该方法必须为 default 或 static
     * static 方法
     * @param runnable
     */
    static void static_method(Runnable runnable) {
        new Thread(runnable).start();
    }

    /**
     * Java8中允许接口中定义非抽象方法 前提该方法必须为 default 或 static
     * default 方法
     * @param str
     */
    default void default_method(String str) {
        System.out.println("is a java8 default method "+str);
    }
}
