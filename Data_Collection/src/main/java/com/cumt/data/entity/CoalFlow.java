package com.cumt.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CoalFlow {
    private Long id;
    private Float currentFlow;
    private Float sumVolume;
    private Float conveyorSpeed;
/*    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat( pattern="yyyy-MM-dd HH:mm:ss")*/
    private LocalDateTime dateTime;
}