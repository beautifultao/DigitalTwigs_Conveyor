package com.cumt.front.service.Impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cumt.front.service.ParamService;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;


public class ParamServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements ParamService<T> {

    @Override
    public void saveParam(T entity) {
        // 添加createTime 字段
        try {
            Field createTimeField = entity.getClass().getDeclaredField("createTime");
            createTimeField.setAccessible(true);
            Object createTimeValue = createTimeField.get(entity);
            if (createTimeValue == null) {
                createTimeField.set(entity, LocalDateTime.now());
            }

        } catch (NoSuchFieldException | IllegalAccessException e) {
            // 如果没有 createTime 字段，或者无法访问它，可以选择记录日志或者忽略
            System.out.println("createTime field not found or not accessible in " + entity.getClass().getName());
        }
        save(entity);
    }

    @Override
    public void deleteParamById(Long id) {
        removeById(id);
    }

    @Override
    public List<T> getAllParam() {
        return list();
    }
}
