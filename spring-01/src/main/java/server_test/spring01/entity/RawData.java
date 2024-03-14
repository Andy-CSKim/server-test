package server_test.spring01.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
//@AllArgsConstructor
//@Builder
@Table("raw_data")
public class RawData extends RawDataBase {
    @Id
    private Long id;
    // blob content
    private byte[] content;
    private Long userId;
//    private String fileType;

    @Builder
    public RawData(String fileType, byte[] content, Long userId) {
        super(fileType);
        this.content = content;
        this.userId = userId;
    }
}
