package com.cumt.front.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("algorithm_param")
public class AlgorithmParamPO implements Serializable{
    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private Float algorithmParam1;
    private Float algorithmParam2;
    private Float algorithmParam3;
    private Float algorithmParam4;

    private LocalDateTime createTime;
}
