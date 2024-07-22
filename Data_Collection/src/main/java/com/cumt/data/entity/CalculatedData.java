package com.cumt.data.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class CalculatedData {
    private Float currentFlow;
    private Float sumVolume;
}
