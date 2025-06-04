package shop.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
//import shop.dao.UserRepository;
import shop.dao.CommunicationRepository;
import shop.dao.UserDao;
import shop.dao.UserRepository;
import shop.dto.*;
import shop.model.*;

import java.sql.SQLException;
import java.util.*;


@RestController
public class UserController {

    @Autowired
    private UserDao userDao;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommunicationRepository communicationRepository;
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

    @GetMapping("/profile")
    public final ProfileDto getUserProfile(HttpServletRequest request, HttpServletResponse response) {

        HttpSession session = request.getSession();
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            response.setStatus(401);
            return null;
        }
        User user = userRepository.findById(userId).get();
        ProfileDto profileDto = new ProfileDto();

        profileDto.setId(user.getId());
        profileDto.setUserName(user.getUserName());

        Set<Ad> ads = user.getAds();
        setSellAdDtos(ads, profileDto);

        setBuyAdDtos(user, profileDto);

        return profileDto;
    }



    private static void setSellAdDtos(Set<Ad> ads, ProfileDto profileDto) {
        for (Ad ad : ads) {
            SellAdDto sellAdDto = new SellAdDto();

            sellAdDto.setAdId(ad.getId());
            sellAdDto.setTitle(ad.getTitle());
            sellAdDto.setDescription(ad.getDescription());
            sellAdDto.setPhotos(ad.getPhotos());

            TreeSet<MessageDto> messagesDtos = new TreeSet<>((o1, o2) -> o1.getDate().compareTo(o2.getDate()));
            List<Communication> communications = ad.getCommunications();

            for (int i = 0; i < communications.size(); i++) {
                Communication communication = communications.get(i);
                List<Message> messages = communication.getMessages();
                for (int j = 0; j <messages.size() ; j++) {
                    MessageDto messageDto = new MessageDto();
                    Message message = messages.get(j);
                    messageDto.setDate(message.getDate());
                    messageDto.setSenderName(message.getSender().getUserName());
                    messageDto.setText(message.getText());
                    messageDto.setCommunicationId(message.getCommunication().getId());
                    messagesDtos.add(messageDto);
                }
            }
            sellAdDto.setMessageDtos(messagesDtos);
            profileDto.addSellAdDto(sellAdDto);
        }
    }
    private void setBuyAdDtos(User user, ProfileDto profileDto) {
        Set<Communication> communications = communicationRepository.findByCreator(user);
        for (Communication communication : communications) {
            Ad ad = communication.getAd();
            BuyAdDto buyAdDto = new BuyAdDto();
            TreeSet<MessageDto> messagesDtos = new TreeSet<>((o1, o2) -> o1.getDate().compareTo(o2.getDate()));
            buyAdDto.setAdId(ad.getId());
            buyAdDto.setTitle(ad.getTitle());
            buyAdDto.setDescription(ad.getDescription());
            List<Photo> photos = ad.getPhotos();
            buyAdDto.setPhotos(photos);

            List<Message> messages = communication.getMessages();
            for (Message message : messages) {
                MessageDto messageDto = new MessageDto();
                messageDto.setDate(message.getDate());
                messageDto.setSenderName(message.getSender().getUserName());
                messageDto.setText(message.getText());
                messageDto.setCommunicationId(message.getCommunication().getId());
                messagesDtos.add(messageDto);
            }
            buyAdDto.setMessageDtos(messagesDtos);
            profileDto.addBuyAdDto(buyAdDto);
        }
    }

}
