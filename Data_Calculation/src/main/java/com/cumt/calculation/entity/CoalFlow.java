package com.cumt.calculation.entity;

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
    private LocalDateTime dateTime;
}
