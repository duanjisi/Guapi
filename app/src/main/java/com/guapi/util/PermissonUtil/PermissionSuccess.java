package com.guapi.util.PermissonUtil;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by namee on 2015. 11. 17..
 * Register a method invoked when permission requests are succeeded.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PermissionSuccess {
  int value() default PermissionUtil.PERMISSIONS_REQUEST_CODE;
}
