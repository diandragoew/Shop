package shop.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.model.Photo;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
}
