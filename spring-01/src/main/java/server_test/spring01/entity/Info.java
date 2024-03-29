package server_test.spring01.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
//@AllArgsConstructor
//@Builder
@Table("info")
public class Info extends InfoBase {
    @Id
    private Long id;
//    private String content;
    private Long userId;

    @Builder
    public Info(Long id, String content, Long userId) {
        super(content);
        this.id = id;
        this.userId = userId;
    }

    @Builder
    public Info(String content, Long userId) {
        super(content);
        this.userId = userId;
    }
}
