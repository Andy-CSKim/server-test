package com.blockki.spring01.service;

import com.blockki.spring01.dto.MemberRequestDto;
import com.blockki.spring01.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class ApiService {

    @Autowired
    private MemberRepository memberRepository;

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

}
