package com.blockki.spring01.controller;

import com.blockki.spring01.dto.LengthRequestDto;
import com.blockki.spring01.dto.LengthResponseDto;
import org.springframework.web.bind.annotation.*;

@RestController
public class APIcontroller {
    @GetMapping("/hello")  // http://localhost:8090/hello : path parameter
    public String hello() {
        return "Hello World!";
    }

    // http://localhost:8090/api/length?unit=inch : /length is path parameter --> @PathVariable
    // ?unit=inch is query parameter --> @RequestParam
    @GetMapping("/api/length/{value}")
    public String cvtLength(@PathVariable int value, @RequestParam String unit) {
        if (unit.equals("inch")) {
            return value / 2.54 + " inch";
        }
        else if (unit.equals("feet")) {
            return value / 30.48 + " feet";
        }
        else {
            return "invalid unit";
        }

    }
    // http://localhost:8090/api/length : /length is path parameter --> @PathVariable
    // LengthRequestDto is body parameter --> @RequestBody, json -> java object
    @PostMapping("/api/length")
    public LengthResponseDto cvtLength2(@RequestBody LengthRequestDto lengthRequestDto) {
        LengthResponseDto lengthResponseDto = new LengthResponseDto();

        if (lengthRequestDto.getUnit().equals("inch")) {
            //return lengthRequestDto.getValue() / 2.54 + " inch";
            lengthResponseDto.setResult(lengthRequestDto.getValue() / 2.54 + " inch");
        }
        else if (lengthRequestDto.getUnit().equals("feet")) {
            //return lengthRequestDto.getValue() / 30.48 + " feet";
            lengthResponseDto.setResult(lengthRequestDto.getValue() / 30.48 + " feet");
        }
        else {
            //return "invalid unit";
            lengthResponseDto.setResult("invalid unit");
        }

        return lengthResponseDto;

    }
}
