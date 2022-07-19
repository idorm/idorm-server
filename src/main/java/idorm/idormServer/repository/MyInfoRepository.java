package idorm.idormServer.repository;

import idorm.idormServer.domain.MyInfo;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MyInfoRepository extends JpaRepository<MyInfo, Long> {


}
