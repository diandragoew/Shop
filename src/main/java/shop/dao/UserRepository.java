package shop.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shop.model.User;

import javax.xml.crypto.Data;


@Repository
public interface UserRepository extends JpaRepository<User,Long> {

}
