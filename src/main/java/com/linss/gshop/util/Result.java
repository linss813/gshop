package com.linss.gshop.util;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Result<T> {
    private boolean success;
    private String message;
    private T data;

    // 无参构造函数，支持Jackson反序列化
    public Result() {
    }

    // 构造方法
    public Result(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    // 简化构造方法（成功时）
    public static <T> Result<T> success(T data) {
        return new Result<>(true, "操作成功", data);
    }

    // 简化构造方法（失败时）
    public static Result<?> error(String message) {
        return new Result<>(false, message, null);
    }
}

