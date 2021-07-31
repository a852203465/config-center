package cn.darkjrong.config.pojo.vo;
import lombok.Data;

import java.io.Serializable;

/**
 * 文件VO
 * @date 2021/06/01
 * @author Rong.Jia
 */
@Data
public class ApplicationVO implements Serializable {

    private static final long serialVersionUID = -7757664175462879159L;

    /**
     * 文件路径
     */
    private String filePath;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 文件内容
     */
    private String fileContent;

    /**
     *  更新时间
     */
    private Long updatedTime;

    /**
     *  文件编号
     */
    private String fileCode;


}
