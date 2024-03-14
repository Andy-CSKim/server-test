package server_test.spring01.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import server_test.spring01.dto.InfoRequestDto;
import server_test.spring01.dto.MemberRequestDto;
import server_test.spring01.entity.Info;
import server_test.spring01.entity.Member;
import server_test.spring01.entity.RawData;
import server_test.spring01.repository.InfoRepository;
import server_test.spring01.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import server_test.spring01.repository.RawDataRepository;

import java.util.HashMap;

@Service
public class ApiService {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private InfoRepository infoRepository;

    @Autowired
    private RawDataRepository rawDataRepository;

    // Object -> List<Member>
    public Iterable<Member> readMembers() {
        return memberRepository.findAll();
    }

    public Member createMember(MemberRequestDto memberRequestDto) {
        // id will be assigned by DB
        return memberRepository.save(memberRequestDto.toEntity());  // without id
    }

    public Member updateMember(long memberId, MemberRequestDto memberRequestDto) {
        if (memberRepository.existsById(memberId)) {
            return memberRepository.save(memberRequestDto.toEntity(memberId));
        }
        return null;
    }

    public String deleteMember(long memberId) {
        if (memberRepository.existsById(memberId)) {
            memberRepository.deleteById(memberId);
            return "OK";
        }
        return "Not found";
    }

    // Info
    public Iterable<Info> readInfos(long userId) {
        return infoRepository.findAllByUserId(userId);
    }

    public Info createInfo(InfoRequestDto infoRequestDto) {

        System.out.println("createInfo: user id = " + infoRequestDto.getUserId());
        // check if user exists
        if (!memberRepository.existsById(infoRequestDto.getUserId())) {
            return null;
        }
        return infoRepository.save(infoRequestDto.toEntity());
    }

    public Info createInfo2(long userId, Object data) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = null;
        try {
            jsonString = mapper.writeValueAsString(data);
            Info newInfo = Info.builder()
//                    .content(data.toString())
                    .content(jsonString)
                    .userId(userId)
                    .build();
            return infoRepository.save(newInfo);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Info updateInfo(long infoId, InfoRequestDto infoRequestDto) {
        if (infoRepository.existsById(infoId)) {
            return infoRepository.save(infoRequestDto.toEntity(infoId));
        }
        return null;
    }

    public String deleteInfo(long infoId) {
        if (infoRepository.existsById(infoId)) {
            infoRepository.deleteById(infoId);
            return "OK";
        }
        return "Not found";
    }

    public String uploadBytes(long userId, String fileType, byte[] data) {
        // find raw_data by userId
        // if not exist, create new raw_data
        // save data to raw_data
        if (rawDataRepository.existsByUserId(userId)) {
            // update
//            rawDataRepository.updateContentByUserId(userId, data);
            RawData rawData = rawDataRepository.findByUserId(userId);
            rawData.setContent(data);
            rawDataRepository.save(rawData);
        } else {
            // create
//            rawDataRepository.saveRawData(userId, fileType, data);
            RawData newRawData = RawData.builder()
                    .userId(userId)
                    .fileType(fileType)
                    .content(data)
                    .build();
            rawDataRepository.save(newRawData);
        }

        return "OK";
    }

    public HashMap<String, Object> downloadBytes(long userId) {

        if (rawDataRepository.existsByUserId(userId)) {
            HashMap<String, Object> result = new HashMap<>();
            RawData rawData = rawDataRepository.findByUserId(userId);
            result.put("fileType", rawData.getFileType());
            result.put("content", rawData.getContent());
            return result;
        }
        return null;
    }
}
