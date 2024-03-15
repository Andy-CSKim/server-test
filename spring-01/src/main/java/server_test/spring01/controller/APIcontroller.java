package server_test.spring01.controller;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import server_test.spring01.dto.InfoRequestDto;
import server_test.spring01.dto.MemberRequestDto;
import server_test.spring01.entity.Info;
import server_test.spring01.entity.Member;
import server_test.spring01.entity.MemberBase;
import server_test.spring01.service.ApiService;
import server_test.spring01.dto.LengthRequestDto;
import server_test.spring01.dto.LengthResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.ByteArrayInputStream;
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
    @GetMapping("/length-path/{value}")
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
    @PostMapping("/length-dto")
    public LengthResponseDto cvtLength(final @Valid @RequestBody LengthRequestDto lengthRequestDto) {
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

    // as to request, class is better than hashmap because of type safety
    @PostMapping("/length-object")
    public  HashMap<String, Object> cvtLength2(@RequestBody HashMap<String, Object> lengthRequestDto) {

        for (Map.Entry<String, Object> entry : lengthRequestDto.entrySet()) {
            // key, value, type
            System.out.println(entry.getKey() + " : " + entry.getValue() + " , " + entry.getValue().getClass().getName());
        }

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
    @GetMapping("/query-test/{id}")
    public Iterable<Member> queryTest(@PathVariable long id, @RequestParam String role) {
        return apiService.queryTest(id, role);
    }

    // Object -> Iterable -> List, Set, Map, ...
    @GetMapping("/users")
    public Iterable<Member> readMembers() {
        return apiService.readMembers();
    }

    // object@front -> dto@controller -> [service] repository.save(dto->entity) -> entity@db
    @PostMapping("/users")
    public Member createMember(@RequestBody MemberRequestDto memberRequestDto) {
        return apiService.createMember(memberRequestDto);
    }
    @PutMapping("/users/{memberId}")
    public Member updateMember(@PathVariable long memberId, @RequestBody MemberRequestDto memberRequestDto) {
        return apiService.updateMember(memberId, memberRequestDto);
    }
    @DeleteMapping("/users/{memberId}")
    public String deleteMember(@PathVariable long memberId) {
        return apiService.deleteMember(memberId);
    }

    // Info
    @GetMapping("/infos/{userId}")
    public Iterable<Info> readInfos(@PathVariable long userId) {
        return apiService.readInfos(userId);
    }

    @PostMapping("/infos")
    public Info createInfo(@RequestBody InfoRequestDto infoRequestDto) {

        // (front by json) {content: `${data}`, user_id: `${userId}`} --> infoRequestDto (content, userId) : error happens
        // name should be same as json key

        System.out.println("post /infos -> createInfo: content=" + infoRequestDto.getContent() + ", user id=" + infoRequestDto.getUserId());
        return apiService.createInfo(infoRequestDto);

    }

    @PostMapping("/infos2/{userId}")
    public Info createInfo2(@PathVariable long userId, @RequestBody Object data) {

        return apiService.createInfo2(userId, data);
    }

    @PutMapping("/infos/{infoId}")
    public Info updateInfo(@PathVariable long infoId, @RequestBody InfoRequestDto infoRequestDto) {
        return apiService.updateInfo(infoId, infoRequestDto);
    }

    @DeleteMapping("/infos/{infoId}")
    public String deleteInfo(@PathVariable long infoId) {
        return apiService.deleteInfo(infoId);
    }

    // path variable, query parameter(file_type), request body
    @PostMapping(path = "/upload-bytes/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadBytes(@PathVariable long userId, @RequestParam String fileType, @RequestPart("file") byte[] data){
        System.out.println("uploadBytes: " + userId + ", " + fileType + ", " + data.length + " bytes");
        return apiService.uploadBytes(userId, fileType, data);
    }

    // path variable, form data (Upload file)
    @PostMapping(path = "/upload-file/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadFile(@PathVariable long userId, @RequestPart("fileType") String fileType, @RequestPart("file") byte[] data) {
        System.out.println("uploadFile: " + userId + ", " + fileType + ", " + data.length + " bytes");
        return apiService.uploadBytes(userId, fileType, data);
    }

    @GetMapping("/download-file/{userId}")
    public ResponseEntity<byte[]> downloadBytes(@PathVariable long userId) {

        HttpHeaders headers = new HttpHeaders();
        HashMap<String, Object> resp = apiService.downloadBytes(userId);
        // according to fileType, return byte[] with either image/* or audio/*
        if (resp.get("fileType").equals("image")) {
            headers.add("Content-Type", "image/*");
            return ResponseEntity.ok()
                    .headers(headers)
                    .body((byte[])resp.get("content"));
        }
        else {  // audio
            headers.add("Content-Type", "audio/*");
            return ResponseEntity.ok()
                    .headers(headers)
                    .body((byte[])resp.get("content"));
        }

    }
}
