package shop.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
//import shop.dao.UserRepository;
import shop.dao.UserDao;
import shop.dto.CreateUserDto;
import shop.model.User;

import java.sql.SQLException;


@RestController
public class UserController {

    @Autowired
    UserDao userDao;
//    @Autowired
//    private UserRepository userRepository;
    @GetMapping("/users/sign-up")
    public void createUser() throws SQLException {
        System.out.println("createUser++++++++++++++++++++++");
//        if (!userRepository.existsByEmail(createUserDto.getEmail())) {
//
//        }
//        User newUser=new User();
//        newUser.setUserName(createUserDto.getUserName());
//        newUser.setEmail(createUserDto.getEmail());
//        newUser.setPassword(createUserDto.getPassword());
//        newUser.setPhone(createUserDto.getPhone());
////        userRepository.save(newUser);
   userDao.addUser(new CreateUserDto());
    }
}
