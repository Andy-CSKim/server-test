package server_test.spring01.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("raw_data")
public class RawDataBase {
//    @Id
//    private Long id;
    // blob content
//    private byte[] content;
//    @Transient
//    private Long userId;
    private String fileType;
}
