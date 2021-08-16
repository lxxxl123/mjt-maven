package com.chen.spring.mvc.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author chenwh
 * @date 2021/8/16
 */
@Builder
@Data
public class Result<T> implements Serializable {

    private int code = 0;
    private String msg;
    private T data;

    public  static <T> Result<T> success(T data){
        return ((Result<T>) Result.builder().data(data).build());
    }

    public static <T> Result<T> error(String msg) {
        return ((Result<T>) Result.builder().code(-1).msg(msg).build());
    }
}
