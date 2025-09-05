package com.example.demo.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResultUtil<T> {
    private boolean success;
    private int code;
    private String message;
    private T data;
    private Long timestamp;

    // 初始化时间戳
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Long getTimestamp() {
        return timestamp == null ? System.currentTimeMillis() : timestamp;
    }

    // 通用成功
    public static <T> ResultUtil<T> success(T data) {
        return ResultUtil.<T>builder()
                .success(true)
                .code(200)
                .message("操作成功")
                .data(data)
                .build();
    }

    public static <T> ResultUtil<T> success(T data, String message) {
        return ResultUtil.<T>builder()
                .success(true)
                .code(200)
                .message(message)
                .data(data)
                .build();
    }

    // 通用失败
    public static <T> ResultUtil<T> error(int code, String message) {
        return ResultUtil.<T>builder()
                .success(false)
                .code(code)
                .message(message)
                .build();
    }

    public static <T> ResultUtil<T> error(String message) {
        return error(500, message);
    }

    // 预设错误
    public static <T> ResultUtil<T> badRequest(String message) {
        return error(400, message);
    }

    public static <T> ResultUtil<T> unauthorized(String message) {
        return error(401, message);
    }

    public static <T> ResultUtil<T> notFound(String message) {
        return error(404, message);
    }

}
