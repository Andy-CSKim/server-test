package server_test.spring01.controller;

import org.springframework.validation.annotation.Validated;
import server_test.spring01.dto.InfoRequestDto;
import server_test.spring01.dto.MemberRequestDto;
import server_test.spring01.service.ApiService;
import server_test.spring01.dto.LengthRequestDto;
import server_test.spring01.dto.LengthResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@RestController
//@Validated  // will check validation of request body
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"}, allowedHeaders = "*")
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
    // as to response, generic object is enough. no need to use LengthResponseDto
    // especially when using database, it is better to use generic object because database returns various types of data
    @PostMapping("/length")
//    public LengthResponseDto cvtLength(@RequestBody LengthRequestDto lengthRequestDto) {
    public Object cvtLength(final @Valid @RequestBody LengthRequestDto lengthRequestDto) {
//        LengthResponseDto lengthResponseDto = new LengthResponseDto();

//        if (lengthRequestDto.getUnit().equals("inch")) {
//            //return lengthRequestDto.getValue() / 2.54 + " inch";
//            lengthResponseDto.setResult(lengthRequestDto.getValue() / 2.54 + " inch");
//        }
//        else if (lengthRequestDto.getUnit().equals("feet")) {
//            //return lengthRequestDto.getValue() / 30.48 + " feet";
//            lengthResponseDto.setResult(lengthRequestDto.getValue() / 30.48 + " feet");
//        }
//        else {
//            //return "invalid unit";
//            lengthResponseDto.setResult("invalid unit");
//        }
//
//        return lengthResponseDto;

        // generic object
        HashMap<String, Object> resp = new HashMap<>();

        if (lengthRequestDto.getUnit().equals("inch")) {
            //return lengthRequestDto.getValue() / 2.54 + " inch";
            resp.put("result", lengthRequestDto.getValue() / 2.54 + " inch");
        }
        else if (lengthRequestDto.getUnit().equals("feet")) {
            //return lengthRequestDto.getValue() / 30.48 + " feet";
            resp.put("result", lengthRequestDto.getValue() / 30.48 + " feet");
        }
        else {
            //return "invalid unit";
            resp.put("result", "invalid unit");
        }

        return resp;

    }

    // as to request, class is better than hashmap because of type safety
    @PostMapping("/length2")
    public Object cvtLength2(@RequestBody HashMap<String, Object> lengthRequestDto) {


//        System.out.println(lengthRequestDto.get("value"));
//        System.out.println(lengthRequestDto.get("unit"));
//        System.out.println(lengthRequestDto.toString());

        for (Map.Entry<String, Object> entry : lengthRequestDto.entrySet()) {
            // key, value, type
            System.out.println(entry.getKey() + " : " + entry.getValue() + " , " + entry.getValue().getClass().getName());
        }

//        return null;
        HashMap<String, Object> resp = new HashMap<>();

        if (lengthRequestDto.get("unit").equals("inch")) {
            //return lengthRequestDto.getValue() / 2.54 + " inch";
            resp.put("result", (int)lengthRequestDto.get("value") / 2.54 + " inch");
        }
        else if (lengthRequestDto.get("unit").equals("feet")) {
            //return lengthRequestDto.getValue() / 30.48 + " feet";
            resp.put("result", (int)lengthRequestDto.get("value") / 30.48 + " feet");
        }
        else {
            //return "invalid unit";
            resp.put("result", "invalid unit");
        }

        return resp;
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

    // Info
    @GetMapping("/infos/{userId}")
    public Object readInfos(@PathVariable long userId) {
        return apiService.readInfos(userId);
    }

    @PostMapping("/infos")
    public Object createInfo(@RequestBody InfoRequestDto infoRequestDto) {
        return apiService.createInfo(infoRequestDto);

    }

    @PostMapping("/infos2/{userId}")
    public Object createInfo2(@PathVariable long userId, @RequestBody Object data) {

        return apiService.createInfo2(userId, data);
    }

    @PutMapping("/infos/{infoId}")
    public Object updateInfo(@PathVariable long infoId, @RequestBody InfoRequestDto infoRequestDto) {
        return apiService.updateInfo(infoId, infoRequestDto);
    }

    @DeleteMapping("/infos/{infoId}")
    public Object deleteInfo(@PathVariable long infoId) {
        return apiService.deleteInfo(infoId);
    }

}
