package server_test.spring01.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import server_test.spring01.entity.Info;

@Data
@NoArgsConstructor
public class InfoRequestDto {
    private String content;
    private Long userId;

    public Info toEntity() {
        return Info.builder()
                .content(content)
                .userId(userId)
                .build();
    }

    public Info toEntity(Long id) {
        return Info.builder()
                .id(id)
                .content(content)
                .userId(userId)
                .build();
    }
}
