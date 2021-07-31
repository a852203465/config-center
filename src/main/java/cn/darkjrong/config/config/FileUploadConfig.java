package cn.darkjrong.config.config;

import cn.hutool.core.io.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.cloud.config.server.environment.NativeEnvironmentProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

import javax.servlet.MultipartConfigElement;
import java.io.File;

/**
 * 文件上传管理
 *
 * @author Rong.Jia
 * @date 2021/06/01
 */
@Configuration
@EnableConfigurationProperties(NativeEnvironmentProperties.class)
public class FileUploadConfig {

    @Autowired
    private NativeEnvironmentProperties nativeEnvironmentProperties;

    @Bean
    public MultipartConfigElement multipartConfigElement() {

        MultipartConfigFactory factory = new MultipartConfigFactory();

        String path = nativeEnvironmentProperties.getSearchLocations()[0];
        File file = new File(path);
        if (!file.isAbsolute()) {
            path = file.getAbsolutePath();
        }

        //路径有可能限制
        FileUtil.mkdir(path);

        factory.setLocation(path);

        // 当上传文件达到10MB的时候进行磁盘写入
        factory.setFileSizeThreshold(DataSize.ofMegabytes(10));

        /*
         * 设置文件大小限制
         * 大小：KB,MB
         */
        factory.setMaxFileSize(DataSize.ofMegabytes(1024));

        //设置总上传数据总大小
        factory.setMaxRequestSize(DataSize.ofMegabytes(1024));

        return factory.createMultipartConfig();
    }



}
