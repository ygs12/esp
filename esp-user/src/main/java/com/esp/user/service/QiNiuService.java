package com.esp.user.service;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;

import java.io.File;
import java.io.InputStream;

/**
 * @ProjectName: qiniu-demo
 * @Auther: GERRY
 * @Date: 2018/11/19 20:37
 * @Description:
 */
public interface QiNiuService {
    // 把一个文件直接上传的服务器
    Response uploadFile(File file) throws QiniuException;
    // 把一个文件对应流上传到服务器
    Response uploadFile(InputStream inputStream) throws QiniuException;
    // 根据一个key删除对应文件
    Response delete(String key) throws QiniuException;
}
