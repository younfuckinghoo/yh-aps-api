package com.jeesite.modules.algorithm.utils;


import org.hibernate.validator.HibernateValidator;
import org.springframework.util.ReflectionUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ValidationUtil {

    //   static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    private static volatile ValidationUtil instance;

    private ValidationUtil() {
        ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class)
                .configure()
                //failFast：只要出现校验失败的情况，就立即结束校验，不再进行后续的校验。
                .failFast(true)
                .buildValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }

    private Validator validator;

    public static ValidationUtil getInstance() {
        if (instance == null) {
            synchronized (ValidationUtil.class) {
                if (instance == null) {
                    instance = new ValidationUtil();
                }
            }
        }
        return instance;

    }

  /*  public Validator validator() {
        ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class)
                .configure()
                //failFast：只要出现校验失败的情况，就立即结束校验，不再进行后续的校验。
                .failFast(true)
                .buildValidatorFactory();
        return validatorFactory.getValidator();
    }*/

    public <T> List<String> valid(T t) {
        Set<ConstraintViolation<T>> errors = this.validator.validate(t);
        return errors.stream().map(error -> {
            String message = error.getMessage();
            Map<String, Object> map = this.findVariables(message, t);
            return SpElUtil.parseSpElTemplate(message, map);
        }).collect(Collectors.toList());
    }

    // 正则表达式匹配提示信息中的SpEl字段名 并从实体类中提取对应字段值
    private <T> Map<String, Object> findVariables(String message, T t) {
        Map<String, Object> map = new HashMap<>();
        String reg = "(#\\{#)(.*?)(})";
        Matcher matcher = Pattern.compile(reg).matcher(message);
        List<String> keyList = new ArrayList<>();
        while (matcher.find()) {
            String key = matcher.group(2);
            keyList.add(key);
            Field field = ReflectionUtils.findField(t.getClass(), key);
            ReflectionUtils.makeAccessible(field);
            Object value = ReflectionUtils.getField(field, t);
            map.put(key, value);
        }
        return map;

    }


}
