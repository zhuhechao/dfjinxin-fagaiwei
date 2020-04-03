package io.dfjinxin.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Desc:
 * @Author: z.h.c
 * @Date: 2020/4/3 11:01
 * @Version: 1.0
 */

@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MyLogPointCut {

    String desc() default "this no desc !";
    String name() default "the name is null !";
}
