package com.esp.user.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @ProjectName: user-service
 * @Auther: GERRY
 * @Date: 2018/11/19 21:33
 * @Description:
 */
@Getter
@Setter
@ToString
public class QiniuPutRet {
    private String hash;
    private String key;
    private String bucket;
    private String width;
    private String height;
}
