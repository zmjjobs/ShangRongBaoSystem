package com.zmj.srb.core.constant;

public enum UserInfoStatusEnum {
    NORMAL(1,"正常"),
    LOCKED(0,"锁定");

    /** 操作码 */
    private final Integer code;
    /** 描述 */
    private final String desc;

    UserInfoStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
