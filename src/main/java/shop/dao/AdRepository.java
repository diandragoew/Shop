package shop.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shop.model.Ad;

@Repository
public interface AdRepository extends JpaRepository<Ad, Long> {
}
