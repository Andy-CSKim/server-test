package com.blockki.spring01.controller;

import com.blockki.spring01.dto.MemberRequestDto;
import com.blockki.spring01.service.ApiService;
import com.blockki.spring01.dto.LengthRequestDto;
import com.blockki.spring01.dto.LengthResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class APIcontroller {

    @Autowired
    private ApiService apiService;

    @GetMapping("/hello")  // http://localhost:8090/hello : path parameter
    public String hello() {
        return "Hello World!";
    }

    // http://localhost:8090/api/length?unit=inch : /length is path parameter --> @PathVariable
    // ?unit=inch is query parameter --> @RequestParam
    @GetMapping("/length/{value}")
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
    @PostMapping("/length")
    public LengthResponseDto cvtLength(@RequestBody LengthRequestDto lengthRequestDto) {
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

    @PostMapping("/length2")
    public Object cvtLength2(@RequestBody HashMap<String, Object> lengthRequestDto) {


//        System.out.println(lengthRequestDto.get("value"));
//        System.out.println(lengthRequestDto.get("unit"));
//        System.out.println(lengthRequestDto.toString());

        for (Map.Entry<String, Object> entry : lengthRequestDto.entrySet()) {
            // key, value, type
            System.out.println(entry.getKey() + " : " + entry.getValue() + " , " + entry.getValue().getClass().getName());
        }

        return null;
    }

    // CRUD api
    @GetMapping("/users")
    public Object readMembers() {
        return apiService.readMembers();
    }
    @PostMapping("/users")
    public Object createMember(@RequestBody MemberRequestDto memberRequestDto) {
        return apiService.createMember(memberRequestDto);
    }
    @PutMapping("/users/{memberId}")
    public Object updateMember(@PathVariable long memberId, @RequestBody MemberRequestDto memberRequestDto) {
        return apiService.updateMember(memberId, memberRequestDto);
    }
    @DeleteMapping("/users/{memberId}")
    public Object deleteMember(@PathVariable long memberId) {
        return apiService.deleteMember(memberId);
    }

}
