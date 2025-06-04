package shop.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import shop.model.Ad;
import shop.model.Communication;
import shop.model.Message;
import shop.model.User;

import java.util.Set;

@Repository
public interface CommunicationRepository extends JpaRepository<Communication, Long> {

    Communication findByCreatorAndAd(User creator, Ad ad);
    Set<Communication> findByCreator(User creator);
}
