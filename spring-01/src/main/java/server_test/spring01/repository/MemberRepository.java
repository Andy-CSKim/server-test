package server_test.spring01.repository;

import server_test.spring01.entity.Member;
import org.springframework.data.repository.CrudRepository;
public interface MemberRepository extends CrudRepository<Member, Long>{
}
