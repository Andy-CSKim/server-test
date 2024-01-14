package server_test.spring01.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@Table("member")
public class Member {
    @Id
    private Long id;
    private String name;
    private String role;


    @Builder
    public Member(Long id, String name, String role) {
        this.id = id;
        this.name = name;
        this.role = role;
    }

    //@Builder
    public Member(String name, String role) {
        this.name = name;
        this.role = role;
    }

}
