package shop.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import shop.model.Communication;
import shop.model.Message;

@Repository
public interface CommunicationRepository extends JpaRepository<Communication, Long> {

}
