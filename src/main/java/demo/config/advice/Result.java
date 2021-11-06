package demo.config.advice;

import lombok.Data;

/**
 * 统一返回参数格式
 * @author admin
 * 2021/8/1621:44
 **/
@Data
public class Result<T> {

    private Boolean success;

    private T result;
}
