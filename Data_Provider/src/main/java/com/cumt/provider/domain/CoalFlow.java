package com.cumt.provider.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("coal_info")
@AllArgsConstructor
public class CoalFlow {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private Float currentFlow;
    private Float sumVolume;
    private Float conveyorSpeed;
/*    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat( pattern="yyyy-MM-dd HH:mm:ss")*/
    private LocalDateTime dateTime;
}