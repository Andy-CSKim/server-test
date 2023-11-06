package com.blockki.spring01.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.blockki.spring01.entity.Member;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberRequestDto {
    private String name;
    private String role;

    public Member toEntity() {
        return Member.builder()
                .name(name)
                .role(role)
                .build();
    }
    public Member toEntity(Long id) {
        return Member.builder()
                .id(id)
                .name(name)
                .role(role)
                .build();
    }

}
