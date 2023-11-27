package shop.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
//import shop.dao.UserRepository;
import shop.dao.UserDao;
import shop.dao.UserRepository;
import shop.dto.CreateUserDto;
import shop.model.User;

import java.sql.SQLException;


@RestController
public class UserController {

    @Autowired
    UserDao userDao;
    @Autowired
    private UserRepository userRepository;
    @GetMapping("/users/sign-up")
    public void createUser() throws SQLException {
        System.out.println("createUser++++++++++++++++++++++");
//        if (!userRepository.eexistsByEmail(createUserDto.getEmail())) {
        CreateUserDto newC=new CreateUserDto();
        User newUser=new User();
        newUser.setUserName(newC.getUserName());
        newUser.setEmail(newC.getEmail());
        newUser.setPassword(newC.getPassword());
        newUser.setPhone(newC.getPhone());
        User nUser = userRepository.save(newUser);
//    }
//   userDao.addUser(new CreateUserDto());
    }
}
