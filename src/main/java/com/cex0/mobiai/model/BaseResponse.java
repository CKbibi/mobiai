package com.cex0.mobiai.model;

import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * @Auther: wodenvyoujiaoshaxiong
 * @Date: 2019/12/26 00:48
 * @Description:
 */
@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse<T> {

    /**
     * Response status
     */
    private Integer status;

    /**
     * Response message
     */
    private String message;

    /**
     * Response development message
     */
    private String devMessage;

    /**
     * Response data
     */
    private T data;


    public BaseResponse(Integer status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }


    /**
     * 创建返回message和data，结果200 (Default status is 200)
     *
     * @param data      result data
     * @param message   result message
     * @return          ok result with message and data
     */
    @NonNull
    public static <T> BaseResponse<T> ok(@Nullable String message, @Nullable T data) {
        return new BaseResponse<>(HttpStatus.OK.value(), message, data);
    }


    /**
     * 创建返回message，结果200 (Default status is 200)
     *
     * @param message   result message
     * @return          ok result with message only
     */
    @NonNull
    public static <T> BaseResponse<T> ok(@Nullable String message) {
        return ok(message, null);
    }

    /**
     * 创建返回data，结果200 (Default status is 200)
     *
     * @param data      data to response
     * @param <T>       data type
     * @return          base response with data
     */
    public static <T> BaseResponse<T> ok(@NonNull T data) {
        return new BaseResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), data);
    }
}
