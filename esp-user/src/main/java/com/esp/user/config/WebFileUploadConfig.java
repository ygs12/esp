package com.esp.user.config;

import com.google.gson.Gson;
import com.qiniu.common.Zone;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ProjectName: qiniu-demo
 * @Auther: GERRY
 * @Date: 2018/11/19 20:24
 * @Description:
 */
@Configuration
public class WebFileUploadConfig {
    /**
     * 创建华东区域
     */
    @Bean
    public com.qiniu.storage.Configuration qiniuConfig() {
        return new com.qiniu.storage.Configuration(Zone.zone0());
    }

    /**
     * 创建一个上传工具的实例
     */
    @Bean
    public UploadManager uploadManager(com.qiniu.storage.Configuration config) {
        return new UploadManager(config);
    }

    @Value("${qiniu.accessKey}")
    private String accessKey;

    @Value("${qiniu.secretKey}")
    private String secretKey;

    /**
     * 创建上传的凭证
     */
    @Bean
    public Auth auth() {
        return Auth.create(accessKey, secretKey);
    }

    /**
     * 创建七牛空间管理实例
     */
    @Bean
    public BucketManager bucketManager(Auth auth, com.qiniu.storage.Configuration configuration) {
        return new BucketManager(auth, configuration);
    }

    /**
     * 实例化Goson对象
     * @return
     */
    @Bean
    public Gson gson() {
        return new Gson();
    }
}
