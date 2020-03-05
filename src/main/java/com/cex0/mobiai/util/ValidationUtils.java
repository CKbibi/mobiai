package com.cex0.mobiai.util;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.FieldError;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.*;

/**
 * Object validation utilities.
 *
 * @author Cex0
 * @date 2020/03/05
 */
public class ValidationUtils {

    private static Validator VALIDATOR;

    private ValidationUtils() {}


    /**
     * 获取验证获取创建
     *
     * @return validator
     */
    @NonNull
    public static Validator getValidatorOrCreate() {
        if (VALIDATOR == null) {
            synchronized (ValidationUtils.class) {
                VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();
            }
        }

        return VALIDATOR;
    }


    /**
     * 手工验证bean。
     *
     * @param obj
     * @param groups
     */
    public static void validate(Object obj, Class<?>... groups) {

        Validator validator = getValidatorOrCreate();

        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(obj, groups);

        if (!CollectionUtils.isEmpty(constraintViolations)) {
            throw new ConstraintViolationException(constraintViolations);
        }
    }


    /**
     * 将字段验证错误转换为标准的map型，key:value = field:message
     *
     * @param constraintViolations constraint violations(contain error information)
     * @return error detail map
     */
    @NonNull
    public static Map<String, String> mapWithValidError(Set<ConstraintViolation<?>> constraintViolations) {
        if (CollectionUtils.isEmpty(constraintViolations)) {
            return Collections.emptyMap();
        }

        Map<String, String> errMap = new HashMap<>();

        constraintViolations.forEach(
                constraintViolation ->
                        errMap.put(constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage()));

        return errMap;
    }


    /**
     * 将字段验证错误转换为标准的map型，key:value = field:message
     *
     * @param fieldErrors 字段错误组
     * @return 如果返回null，则表示未出现错误
     */
    public static Map<String, String> mapWithFieldError(@Nullable List<FieldError> fieldErrors) {
        if (CollectionUtils.isEmpty(fieldErrors)) {
            return Collections.emptyMap();
        }

        Map<String, String> errMap = new HashMap<>(4);
        fieldErrors.forEach(filedError -> errMap.put(filedError.getField(), filedError.getDefaultMessage()));
        return errMap;
    }
}
