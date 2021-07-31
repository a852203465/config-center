package cn.darkjrong.config.utils;

import cn.hutool.core.io.FileUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

/**
 *  文件工具类
 * @author Rong.Jia
 * @date 2021/06/02 19:56
 */
@Slf4j
public class FileUtils {

    public static void del(File file) {
        try {
            FileUtil.del(file);
        }catch (Exception e) {
            log.error("del {}", e.getMessage());
        }
    }


}
