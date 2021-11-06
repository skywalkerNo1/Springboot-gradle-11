package demo.config.advice;

import demo.utils.JacksonUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 全局返回值统一包装
 * @author admin
 * 2021/8/1621:46
 **/
@ControllerAdvice(basePackages = "demo.controller")
public class ResponseHandler<T> implements ResponseBodyAdvice<T> {

    /**
     * 判断哪些返回值需要进行处理
     * @param returnType
     * @param converterType
     * @return
     */
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return returnType.getParameterType() != Result.class;
    }

    @Override
    public T beforeBodyWrite(T body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        Result<T> res = new Result<>();
        res.setSuccess(true);
        res.setResult(body);
        if (body instanceof String) {
            return (T) JacksonUtils.objToJson(res);
        }
        return (T) res;
    }
}
