package com.blockki.spring01.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LengthDto {
    private int value;
    private String unit;

    // getter & setter
    // public int getValue()
    // public void setValue(int value)
}
