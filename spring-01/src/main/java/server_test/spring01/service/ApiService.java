package server_test.spring01.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import server_test.spring01.dto.InfoRequestDto;
import server_test.spring01.dto.MemberRequestDto;
import server_test.spring01.entity.Info;
import server_test.spring01.repository.InfoRepository;
import server_test.spring01.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class ApiService {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private InfoRepository infoRepository;

    public Object readMembers() {
        return memberRepository.findAll();
    }

    public Object createMember(MemberRequestDto memberRequestDto) {
        // id will be assigned by DB
        return memberRepository.save(memberRequestDto.toEntity());  // without id
    }

    public Object updateMember(long memberId, MemberRequestDto memberRequestDto) {
        if (memberRepository.existsById(memberId)) {
            return memberRepository.save(memberRequestDto.toEntity(memberId));
        }
        return null;
    }

    public Object deleteMember(long memberId) {
        if (memberRepository.existsById(memberId)) {
            memberRepository.deleteById(memberId);
            return "OK";
        }
        return "Not found";
    }

    // Info
    public Object readInfos(long userId) {
        return infoRepository.findAllByUserId(userId);
    }

    public Object createInfo(InfoRequestDto infoRequestDto) {
        return infoRepository.save(infoRequestDto.toEntity());
    }

    public Object createInfo2(long userId, Object data) {
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
            return "Error";
        }

    }

    public Object updateInfo(long infoId, InfoRequestDto infoRequestDto) {
        if (infoRepository.existsById(infoId)) {
            return infoRepository.save(infoRequestDto.toEntity(infoId));
        }
        return null;
    }

    public Object deleteInfo(long infoId) {
        if (infoRepository.existsById(infoId)) {
            infoRepository.deleteById(infoId);
            return "OK";
        }
        return "Not found";
    }

}
