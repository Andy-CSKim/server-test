package server_test.spring01.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
public class LengthRequestDto {

    // validation
    @DecimalMax(value = "1000000")
    private int value;
    @NotEmpty    // @NotNull, @NotBlank
    @Pattern(regexp = "^[a-zA-Z]*$")  // string only
    private String unit;

    // getter & setter
    // public int getValue()
    // public void setValue(int value)
}
