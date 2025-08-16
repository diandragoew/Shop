package shop.dao;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import shop.model.Photo;

public interface PhotoRepository extends JpaRepository<Photo, Long> {

    @Transactional
    void deleteAllByAd_Id(Long adId);
}
