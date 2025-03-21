package shop.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.model.Ad;

public interface AdRepository extends JpaRepository<Ad, Long> {
}
