package server_test.spring01.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;

@Data
@NoArgsConstructor
//@AllArgsConstructor
//@Builder
@Table("member")
public class Member {
    @Id
    private Long id;
    private String name;
    private String role;


    // xxxBase doesn't have @Id that has td do with "ResultSet contains id multiple times"
    // idColumn means foreign key in child entity while keyColumn means primary key in parent entity
    @MappedCollection(keyColumn = "id", idColumn = "user_id")
//    private List<Info> infos; //  = new ArrayList<>();
    private List<InfoBase> infos; //  = new ArrayList<>();

    @MappedCollection(keyColumn = "id", idColumn = "user_id")
//    private RawData rawData;
    private RawDataBase rawDataBase;

//    public Member(Long id, String name, String role, RawData rawData) {
//        this.id = id;
//        this.name = name;
//        this.role = role;
//        this.rawData = rawData;
//    }
    @Builder
    public Member(Long id, String name, String role) {
        this.id = id;
        this.name = name;
        this.role = role;
    }
//
//    //@Builder
//    public Member(String name, String role) {
//        this.name = name;
//        this.role = role;
//    }

}
