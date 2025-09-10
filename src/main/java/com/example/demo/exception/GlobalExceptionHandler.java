package com.example.demo.exception;

import com.example.demo.resp.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import javax.naming.AuthenticationException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 捕获所有未处理的 Exception
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResultUtil<Void> handleException(Exception e) {
        log.error("【系统异常】", e);
        return ResultUtil.error("系统繁忙，请稍后再试");
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ResultUtil<?>> handleAuthException(AuthenticationException e) {
        return ResponseEntity.status(401).body(ResultUtil.error("认证失败"));
    }

    /**
     * 捕获自定义业务异常
     */
    @ExceptionHandler(BizException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResultUtil<Void> handleBizException(BizException e) {
        log.warn("【业务异常】{}", e.getMessage());
        return ResultUtil.error(e.getCode(), e.getMessage());
    }

    /**
     * 参数校验异常（@Valid）
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResultUtil<Void> handleValidException(MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();
        StringBuilder errorMsg = new StringBuilder();
        for (FieldError error : result.getFieldErrors()) {
            errorMsg.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("; ");
        }
        log.warn("【参数校验异常】{}", errorMsg);
        return ResultUtil.badRequest(errorMsg.toString());
    }

    /**
     * 参数类型不匹配（如 String 传给 Long）
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResultUtil<Void> handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        String errorMsg = "参数类型错误: " + e.getName() + " 应为 " + e.getRequiredType().getSimpleName();
        log.warn("【类型不匹配】{}", errorMsg);
        return ResultUtil.badRequest(errorMsg);
    }

    /**
     * 请求方法不支持（POST 调成 GET 等）
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ResultUtil<Void> handleMethodNotSupported(HttpRequestMethodNotSupportedException e) {
        return ResultUtil.error(405, "请求方法不支持: " + e.getMethod());
    }

    /**
     * REST 调用超时或 4xx/5xx
     */
    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<ResultUtil<Void>> handleWebClientException(WebClientResponseException e) {
        int status = e.getStatusCode().value();
        String message = "远程服务错误: " + e.getResponseBodyAsString();

        log.error("【WebClient 异常】{} {}", status, message);

        ResultUtil<Void> result = status == 404 ?
                ResultUtil.notFound("远程接口不存在") :
                ResultUtil.error(status, message);

        return ResponseEntity.status(status).body(result);
    }

    /**
     * 处理空指针等严重异常（可选）
     */
    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResultUtil<Void> handleNPE(NullPointerException e) {
        log.error("【空指针异常】", e);
        return ResultUtil.error("系统内部错误，请联系管理员");
    }
}
