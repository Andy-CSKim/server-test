package server_test.spring01.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import server_test.spring01.entity.RawData;

public interface RawDataRepository extends CrudRepository<RawData, Long>{

    boolean existsByUserId(long userId);

//    void updateContentByUserId(long userId, byte[] data);

//    void saveRawData(long userId, String fileType, byte[] data);

    RawData findByUserId(long userId);
}
