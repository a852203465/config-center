package cn.darkjrong.config.utils;

import cn.hutool.core.io.FileUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * application.yml文件路径配置文件处理器
 *
 * @author Rong.Jia
 * @date 2021/06/01 23:28
 */
@Slf4j
@Data
public class PropertiesUtils {

    /**
     * 读取配置文件
     * @param profilePath 文件目录
     * @author Rong.Jia
     * @date 2021/06/01 23:28
     * @return 文件列表
     */
    public static List<File> getProperties(String profilePath) {
        List<File> ymlList = new ArrayList<>();
        File directory = new File(profilePath);

        if (directory.isDirectory()) {

            File[] ymlFiles = directory.listFiles();
            if (ymlFiles != null) {
                for (File file : ymlFiles) {
                    if (!file.isDirectory()) {
                        if (file.getName().endsWith(".yml")) {
                            ymlList.add(file);
                        }
                    }
                }
            } else {
                log.error("getProperties() 该目录下没有文件!");
            }
        } else {
            log.error("getProperties() 这不是文件夹!");
        }
        return ymlList;
    }

    /**
     * 获取单个yml文件
     * @param profilePath 文件目录
     * @param fileName 文件名
     * @author Rong.Jia
     * @date 2021/06/01 23:28
     * @return 文件路径
     */
    public static String getFilePath(String profilePath, String fileName) {
        return profilePath + fileName;
    }

    /**
     * 添加配置文件
     * @param profilePath 文件目录
     * @param file 文件
     * @author Rong.Jia
     * @date 2021/06/01 23:28
     * @return 成功与否
     */
    public static boolean addFile(String profilePath, File file) {
        File directory = new File(profilePath);
        if (directory.exists() && directory.isDirectory()) {
            File newFile = new File(profilePath + file.getName());
            if (newFile.exists()) {
                return false;
            }
            file.renameTo(newFile);
        }

        return true;
    }

    /**
     * 删除配置文件
     * @param fileName 文件名
     * @param profilePath 文件目录
     * @author Rong.Jia
     * @date 2021/06/01 23:28
     */
    public static void deleteFile(String profilePath, String fileName) {
        File file = new File(profilePath + fileName);
        if (FileUtil.exist(file)) {
            file.delete();
        }
    }


}
