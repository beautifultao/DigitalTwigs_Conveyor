package com.cumt.common.result;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class Result<T> implements Serializable {
    private Integer code;
    private String message;
    private T data;

    public static <E>Result<E> success(E data){
        return new Result<>(0, "success", data);
    }

    public static <E>Result<E> success(){
        return new Result<>(0,"success",null);
    }

    public static <E>Result<E> error(String message){
        return new Result<>(1, message, null);
    }
}
