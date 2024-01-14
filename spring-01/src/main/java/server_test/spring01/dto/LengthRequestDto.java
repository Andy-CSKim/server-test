package server_test.spring01.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LengthRequestDto {
    private int value;
    private String unit;

    // getter & setter
    // public int getValue()
    // public void setValue(int value)
}
