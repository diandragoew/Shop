package shop.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
//import shop.dao.UserRepository;
import shop.dao.UserDao;
import shop.dao.UserRepository;
import shop.dto.CreateUserDto;
import shop.dto.LoginDto;
import shop.model.Ad;
import shop.model.User;

import java.sql.SQLException;
import java.util.Set;


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
        CreateUserDto newC = new CreateUserDto();
        User newUser = new User();
        newUser.setUserName(newC.getUserName());
        newUser.setEmail(newC.getEmail());
        newUser.setPassword(newC.getPassword());
        newUser.setPhone(newC.getPhone());
        User nUser = userRepository.save(newUser);
//    }
//   userDao.addUser(new CreateUserDto());
    }

    @PostMapping("/login")
    public void login(@RequestBody LoginDto LoginUser, HttpServletRequest request) throws SQLException {
        User user = userRepository.findByUserNameAndPassword(LoginUser.getUserName(), LoginUser.getPassword());
        HttpSession session = request.getSession();
        session.setAttribute("userId", user.getId());
        session.setMaxInactiveInterval(3000);
    }

    @Transactional
    @GetMapping("/profile")
    public User getUserProfile(HttpServletRequest request, HttpServletResponse response) {

        HttpSession session = request.getSession();
        Long userId = (Long) session.getAttribute("userId");
        if(userId == null) {
            response.setStatus(401);
            return null;
        }
       User user = userRepository.findById(userId).get();
        return user;
    }
}
