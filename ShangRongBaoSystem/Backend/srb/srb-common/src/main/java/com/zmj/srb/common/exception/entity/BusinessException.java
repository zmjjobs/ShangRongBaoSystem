package com.zmj.srb.common.exception.entity;

import com.zmj.srb.common.result.ResponseEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 业务异常类
 * RuntimeException是运行时异常，非检查异常，这样代码好看简洁，省去了try-catch
 * @Author : mjzhud
 * @create 2023/11/9 16:59
 */
@Data
@NoArgsConstructor //无参构造函数
public class BusinessException extends RuntimeException {
    /** 错误码 */
    private Integer code;
    /** 错误消息 */
    private String message;

    /**
     *
     * @param message 错误消息
     */
    public BusinessException(String message) {
        this.message = message;
    }

    /**
     *
     * @param message 错误消息
     * @param code 错误码
     */
    public BusinessException(String message, Integer code) {
        this.message = message;
        this.code = code;
    }

    /**
     *
     * @param message 错误消息
     * @param code 错误码
     * @param cause 原始异常对象
     */
    public BusinessException(String message, Integer code, Throwable cause) {
        super(cause);
        this.message = message;
        this.code = code;
    }

    /**
     *
     * @param resultCodeEnum 接收枚举类型
     */
    public BusinessException(ResponseEnum resultCodeEnum) {
        this.message = resultCodeEnum.getMessage();
        this.code = resultCodeEnum.getCode();
    }

    /**
     *
     * @param resultCodeEnum 接收枚举类型
     * @param cause 原始异常对象
     */
    public BusinessException(ResponseEnum resultCodeEnum, Throwable cause) {
        super(cause);
        this.message = resultCodeEnum.getMessage();
        this.code = resultCodeEnum.getCode();
    }
}
