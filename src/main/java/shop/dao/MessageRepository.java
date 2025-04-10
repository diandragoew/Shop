package shop.dao;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import shop.model.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query(value = "SELECT * \n" +
            "FROM shop.messages \n" +
            "WHERE (sender_id = :userId OR recipient_id = :userId) AND ad_id = :adId\n" +
            "LIMIT 1;\n", nativeQuery = true)
    public Message takeMessage(Long userId, Long adId);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO shop.messages (sender_id, recipient_id, ad_id, text,communication_id, date) VALUES (:senderId,:recipientId, :adId, :text,:communicationId, NOW())", nativeQuery = true)
    void saveMessage(Long senderId,Long recipientId, Long adId, String text,Long communicationId);



}
