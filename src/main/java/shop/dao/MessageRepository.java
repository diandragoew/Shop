package shop.dao;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import shop.model.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO shop.messages (user_id, ad_id, text, date) VALUES (:userId, :adId, :text, NOW())", nativeQuery = true)
    void saveMessage(Long userId, Long adId, String text);
}
