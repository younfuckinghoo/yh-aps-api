package com.jeesite.modules.algorithm.utils;

import java.lang.reflect.Method;

/**
 * 基于org.springframework.util.ReflectionUtils 的反射工具类
 */
public class ReflectionUtils {


	public static <T> T callMethod(Object instance, String methodName, Class<T> resultClazz, Object... args) {

		// 里面应该已经处理过找不到就抛异常的逻辑 不需要再去判断了
		Method method = org.springframework.util.ReflectionUtils.findMethod(instance.getClass(), methodName);
		return (T) org.springframework.util.ReflectionUtils.invokeMethod(method, instance);
	}

	public static <T> T callMethodWithArgs(Object instance, String methodName, Class<T> resultClazz, Object... args) {
		Class[] paramTypes = new Class[args.length];
		for (int i = 0; i < args.length; i++) {
			paramTypes[i] = args[i].getClass();
		}
		// 里面应该已经处理过找不到就抛异常的逻辑 不需要再去判断了
		Method method = org.springframework.util.ReflectionUtils.findMethod(instance.getClass(), methodName, paramTypes);
		return (T) org.springframework.util.ReflectionUtils.invokeMethod(method, instance, args);
	}

}
