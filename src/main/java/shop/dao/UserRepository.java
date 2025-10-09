package shop.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shop.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import javax.xml.crypto.Data;


@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    public  User findByUserNameAndPassword(String userName, String password);
    // New methods to check for existence by username or email
    boolean existsByUserName(String userName);
    boolean existsByEmail(String email);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.favoriteAds WHERE u.id = :id")
    java.util.Optional<shop.model.User> findByIdWithFavorites(@Param("id") Long id);
}
