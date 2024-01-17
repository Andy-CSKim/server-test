package server_test.spring01.repository;

import org.springframework.data.repository.CrudRepository;
import server_test.spring01.entity.Info;

import java.util.List;

public interface InfoRepository extends CrudRepository<Info, Long> {

    // error when return object
    List<Info> findAllByUserId(long userId);
}
