package com.esp.user.exception;

/**
 * @ProjectName: user-service
 * @Auther: GERRY
 * @Date: 2018/11/15 16:34
 * @Description: 自定义用户异常
 */
public class UserException extends RuntimeException {
    private UserType type;

    public UserException() {

    }

    public UserException(UserType type,String message) {
        super(message);
        this.type = type;
    }

    public UserType type() {
        return type;
    }


    public enum UserType {
        ACCOUNT_AND_PASSWORD_IS_NULL,
        USER_AUTH_FAIL,
        USER_NOT_LOGIN,
        USER_NOT_FOUND
    }
}
