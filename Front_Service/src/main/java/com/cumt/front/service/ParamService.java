package com.cumt.front.service;

import java.util.List;

public interface ParamService<T> {
    void saveParam(T entity);
    void deleteParamById(Long id);
    List<T> getAllParam();
}
