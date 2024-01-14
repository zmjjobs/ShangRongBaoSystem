package com.zmj.srb.core.constant;

public enum ExcelOperationTypeEnum {
    ADDITIONAL_RECORD_NO_REPETITION(1,"追加记录不许重复"),
    CLEAN_UP_BEFORE_INSERTING(2,"表被清空后再插入");

    /** 操作码 */
    private final Integer code;
    /** 描述 */
    private final String desc;

    ExcelOperationTypeEnum(Integer code, String desc) {
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
