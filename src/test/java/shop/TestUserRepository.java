package shop;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import shop.dao.UserRepository;
import shop.model.User;


@SpringBootTest
public class TestUserRepository {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void takeUserTest() {
        User user = userRepository.findById(1L).get();

        // Ensure user exists
        Assertions.assertNotNull(user);

    }

}
