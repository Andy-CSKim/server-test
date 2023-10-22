package com.blockki.spring01.controller;

import com.blockki.spring01.dto.LengthDto;
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
    // LengthDto is body parameter --> @RequestBody, json -> java object
    @PostMapping("/api/length")
    public String cvtLength2(@RequestBody LengthDto lengthDto) {

        if (lengthDto.getUnit().equals("inch")) {
            return lengthDto.getValue() / 2.54 + " inch";
        }
        else if (lengthDto.getUnit().equals("feet")) {
            return lengthDto.getValue() / 30.48 + " feet";
        }
        else {
            return "invalid unit";
        }

    }
}
