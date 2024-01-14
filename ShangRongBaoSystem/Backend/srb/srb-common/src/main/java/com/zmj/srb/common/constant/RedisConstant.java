package com.zmj.srb.common.constant;

public class RedisConstant {
    public enum RedisKey{
        SMS_CODE_PREFIX(1,"srb:sms:code","短信验证码前缀");
        private final int index;
        private final String code;
        private final String desc;
        RedisKey(int index, String code, String desc) {
            this.index = index;
            this.code = code;
            this.desc = desc;
        }
        public int getIndex() {
            return index;
        }

        public String getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }
    }
}
