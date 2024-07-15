package com.cumt.front.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CoalFlow {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private Float currentFlow;
    private Float sumVolume;
    private Float conveyorSpeed;
    private LocalDateTime dateTime;
}
