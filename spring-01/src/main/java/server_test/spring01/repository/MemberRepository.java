package server_test.spring01.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import server_test.spring01.entity.Member;

public interface MemberRepository extends CrudRepository<Member, Long>{

    // test query
    // https://docs.spring.io/spring-data/relational/reference/jdbc/query-methods.html
//    @Query("SELECT * FROM member WHERE id = :id")  // OK
    @Query("SELECT * FROM member WHERE role = :role")  // OK
    Iterable<Member> findByQuery(long id, String role);

    Iterable<Member> findByRole(String role);

//    @Query("SELECT id, name, role FROM member WHERE id = :id")  // OK but all columns are returned
//    Iterable<Member> findPartialInfo(long id, String role);

//    @Query("SELECT name, role FROM member")  // NG
//    Iterable<MemberBase> findPartialInfo(long id, String role);
}
