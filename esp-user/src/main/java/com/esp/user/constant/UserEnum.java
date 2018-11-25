package com.esp.user.constant;

import lombok.Getter;

/**
 * @ProjectName: user-service
 * @Auther: GERRY
 * @Date: 2018/11/15 20:32
 * @Description:
 */
@Getter
public enum UserEnum {
    ACTIVE(1, "已激活"),
    DISABLE(0, "未激活");

    private int code;
    private String msg;

    UserEnum(int code, String msg) {
        this.msg = msg;
        this.code = code;
    }
}
