package cn.darkjrong.config.pojo.vo;

import cn.darkjrong.config.enums.ResponseEnum;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

/**
 *  数据格式返回统一
 * @date 2021/06/01
 * @author Rong.Jia
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseVO<T> implements Serializable {

    private static final long serialVersionUID = 3681838956784534606L;

    /**
     * 异常码
     */
    private Integer code;

    /**
     * 描述
     */
    private String message;

    /**
     * 数据
     */
    private T data;

    public ResponseVO() {}

    public ResponseVO(Integer code, String msg) {
        this.code = code;
        this.message = msg;
    }

    public ResponseVO(Integer code, String msg, T data) {
        this.code = code;
        this.message = msg;
        this.data = data;
    }

    public ResponseVO(ResponseEnum responseEnum) {
        this.code = responseEnum.getCode();
        this.message = responseEnum.getMessage();
    }

    public ResponseVO(ResponseEnum responseEnum, T data) {
        this.code = responseEnum.getCode();
        this.message = responseEnum.getMessage();
        this.data = data;
    }

    public static <T> ResponseVO<T> success(){
        return new ResponseVO<>(ResponseEnum.SUCCESS);
    }

    public static <T> ResponseVO<T> success(T data){
        return new ResponseVO<>(ResponseEnum.SUCCESS, data);
    }

    public static <T> ResponseVO<T> error(T data){
        return new ResponseVO<>(ResponseEnum.ERROR, data);
    }

    public static <T> ResponseVO<T> success(int code, String msg){
        return new ResponseVO<>(code, msg);
    }

    public static <T> ResponseVO<T> error(int code, String msg){
        return new ResponseVO<>(code, msg);
    }

    public static <T> ResponseVO<T> error(ResponseEnum responseEnum){
        return new ResponseVO<>(responseEnum);
    }

    public static ResponseVO<?> error(ResponseEnum responseEnum, Object data){
        return new ResponseVO<>(responseEnum, data);
    }

    public static ResponseVO<?> error(BindingResult result, MessageSource messageSource) {

        StringBuffer msg = new StringBuffer();

        //获取错误字段集合
        List<FieldError> fieldErrors = result.getFieldErrors();

        //获取本地locale,zh_CN
        Locale currentLocale = LocaleContextHolder.getLocale();

        //遍历错误字段获取错误消息
        for (FieldError fieldError : fieldErrors) {

            //获取错误信息
            String errorMessage = messageSource.getMessage(fieldError, currentLocale);

            //添加到错误消息集合内
            msg.append(fieldError.getField()).append(StrUtil.COLON).append(errorMessage).append(StrUtil.COMMA);

        }
        return ResponseVO.error(ResponseEnum.PARAMETER_ERROR, msg.toString());
    }

}
