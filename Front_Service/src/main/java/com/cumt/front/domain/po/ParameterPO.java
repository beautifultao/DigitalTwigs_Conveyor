package com.cumt.front.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("parameter_config")
public class ParameterPO implements Serializable{
    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private Float raderParam1;

    private Float raderParam2;

    private Float cameraParam1;

    private Float cameraParam2;

    private Float algorithmParam1;

    private Float algorithmParam2;

    private Integer plcParam1;

    private Integer plcParam2;

    private LocalDateTime createTime;
}
