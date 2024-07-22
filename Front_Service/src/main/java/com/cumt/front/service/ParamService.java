package com.cumt.front.service;

import java.util.List;

/**
 * 保存、删除、获取参数的公共接口
 * @param <T>
 */
public interface ParamService<T> {
    void saveParam(T entity);
    void deleteParamById(Long id);
    List<T> getAllParam();
}
