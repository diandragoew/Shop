package shop.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Repository;
import shop.model.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO shop.messages (sender_id,  text,communication_id, date) VALUES (:senderId,  :text,:communicationId, NOW())", nativeQuery = true)
    void saveMessage(Long senderId,  String text,Long communicationId);



}
