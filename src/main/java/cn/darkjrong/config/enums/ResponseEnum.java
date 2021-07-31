package cn.darkjrong.config.enums;

import org.springframework.http.HttpStatus;

/**
 *  数据信息状态枚举类
 * @date 2021/06/01
 * @author Rong.Jia
 */
public enum ResponseEnum {

    // 成功
    SUCCESS(0,"成功"),

    // 参数不正确
    PARAMETER_ERROR(1, "参数不正确"),

    // 失败
    ERROR(-1, "失败"),

    // 未找到
    NOT_FOUND(HttpStatus.NOT_FOUND.value(), "请求接口不存在"),

    // 请求方式错误
    REQUEST_MODE_ERROR(HttpStatus.METHOD_NOT_ALLOWED.value(), "请求方式错误, 请检查"),

    //媒体类型不支持
    MEDIA_TYPE_NOT_SUPPORTED(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), "媒体类型不支持"),

    NOT_YML(1000,"非yml文件,不可上传"),
    NULL_YML(1001,"当前目录下文件为空"),
    FILE_NOT_NULL(1002,"上传文件不能为空"),
    FILE_EXISTS(1003,"该文件已存在!"),
    UPLOAD_FAILED(1004,"文件上传失败!"),
    FILE_NOT_EXISTS(1005,"该文件已存在!"),




















    ;

    private Integer code;
    private String message;

    ResponseEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
