package com.blockki.spring01.dto;

import com.blockki.spring01.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberResponseDto {
    private Long id;
    private String name;
    private String role;

    public static MemberResponseDto of(Member member) {

        return new MemberResponseDto(member.getId(), member.getName(), member.getRole());
    }
}

