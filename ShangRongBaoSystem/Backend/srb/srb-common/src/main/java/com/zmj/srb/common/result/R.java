package com.zmj.srb.common.result;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author : mjzhud
 * @create 2023/11/7 21:03
 */
@Data
public class R {
    /** 响应状态码 */
    private Integer code;
    /** 响应信息 */
    private String message;
    private Map<String,Object> data = new HashMap<>();

    private R(){}

    /**
     * 返回成功结果
     * @return
     */
    public static R ok(){
        R r = new R();
        r.setCode(ResponseEnum.SUCCESS.getCode());
        r.setMessage(ResponseEnum.SUCCESS.getMessage());
        return r;
    }

    /**
     * 返回错误结果
     * @return
     */
    public static R error(){
        R r = new R();
        r.setCode(ResponseEnum.ERROR.getCode());
        r.setMessage(ResponseEnum.ERROR.getMessage());
        return r;
    }

    /**
     * 设置特定结果
     * 注意：调用时会新建对象，所以必须先于其他方法调用
     *      如果不新建对象，请使用 R.ResponseEnum(ResponseEnum ResponseEnum)
     * @return
     */
    public static R setR(ResponseEnum ResponseEnum){
        R r = new R();
        r.setCode(ResponseEnum.getCode());
        r.setMessage(ResponseEnum.getMessage());
        return r;
    }

    /**
     * 设置特定结果
     * 注意：调用时不新建对象
     *      如果新建对象，请使用 R.setR(ResponseEnum ResponseEnum)
     * @return
     */
    public R ResponseEnum(ResponseEnum ResponseEnum){
        this.setCode(ResponseEnum.getCode());
        this.setMessage(ResponseEnum.getMessage());
        return this;
    }

    public R data(String key,Object value){
        this.data.put(key,value);
        return this;
    }

    public R data(Map<String,Object> map){
        this.setData(map);
        return this;
    }

    /**
     * 设置特定的消息
     * 注意：调用时不新建对象
     * @param message
     * @return
     */
    public R message(String message){
        this.setMessage(message);
        return this;
    }

    /**
     * 设置特定的响应码
     * 注意：调用时不新建对象
     * @param code
     * @return
     */
    public R code(Integer code){
        this.setCode(code);
        return this;
    }
}
