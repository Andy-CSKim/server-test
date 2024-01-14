package com.blockki.spring01.repository;

import com.blockki.spring01.entity.Member;
import org.springframework.data.repository.CrudRepository;
public interface MemberRepository extends CrudRepository<Member, Long>{
}
