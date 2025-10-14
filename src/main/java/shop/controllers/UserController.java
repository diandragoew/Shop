package shop.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
//import shop.dao.UserRepository;
import shop.dao.CommunicationRepository;
import shop.dao.UserDao;
import shop.dao.UserRepository;
import shop.dto.*;
import shop.exceptions.UnauthorizedException;
import shop.model.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

import org.springframework.http.HttpStatus; // Import HttpStatus
import org.springframework.http.ResponseEntity; // Import ResponseEntity
import shop.service.FavoriteService;

@RestController
public class UserController {

    @Autowired
    private UserDao userDao;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommunicationRepository communicationRepository;

    @Autowired
    private FavoriteService favoriteService;

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
    public void login(@RequestBody LoginDto LoginUser, HttpServletRequest request, HttpServletResponse response) throws SQLException {
        User user = userRepository.findByUserNameAndPassword(LoginUser.getUserName(), LoginUser.getPassword());
        HttpSession session = request.getSession();

        if (user == null) {
            response.setStatus(401);
            return;
        }
        session.setAttribute("userId", user.getId());
        session.setMaxInactiveInterval(3000);
        response.setStatus(200);
    }

    @GetMapping("/isLoggedIn")
    public boolean isLoggedIn(HttpServletRequest request) throws SQLException {
        HttpSession session = request.getSession();
        Long loggedUserId = (Long) session.getAttribute("userId");

        if (loggedUserId == null) {
            return false;
        } else {
            return true;
        }
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request) throws SQLException {
        HttpSession session = request.getSession();
        session.invalidate();
    }

    @GetMapping("/profile")
    public final ProfileDto getUserProfile(HttpServletRequest request, HttpServletResponse response) {

        HttpSession session = request.getSession();
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            response.setStatus(401);
            return null;
        }
        //        User user = userRepository.findById(userId).get();
        // Use the new method to fetch the user with their favorite ads
        User user = userRepository.findByIdWithFavorites(userId)
                                  .orElseThrow(() -> new RuntimeException("User not found"));
        ProfileDto profileDto = new ProfileDto();

        profileDto.setId(user.getId());
        profileDto.setUserName(user.getUserName());

        Set<Ad> ads = user.getAds();
        setSellAdDtos(ads, profileDto);

        setBuyAdDtos(user, profileDto);

        return profileDto;
    }

    @GetMapping("/user")
    public CreateUserDto getUser(HttpServletRequest request, HttpServletResponse response) {

        HttpSession session = request.getSession();
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            response.setStatus(401);
            return null;
        }
        User user = userRepository.findById(userId).get();
        CreateUserDto createUserDto = new CreateUserDto();
        createUserDto.setUserName(user.getUserName());
        createUserDto.setEmail(user.getEmail());
        createUserDto.setPhone(user.getPhone());

        return createUserDto;
    }

    @PatchMapping("/edit-password")
    public void editPassword(@RequestBody EditPasswordDto editPasswordDto, HttpServletRequest request, HttpServletResponse response)
        throws SQLException, IOException {
        HttpSession session = request.getSession();
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            response.setStatus(401);
            return;
        }
        User user = userRepository.findById(userId).get();

        String oldPassword = editPasswordDto.getPassOld();
        String newPassword = editPasswordDto.getPassNew();
        String newPasswordConfirm = editPasswordDto.getPassNewConfirm();

        if ("".equalsIgnoreCase(oldPassword) || "".equalsIgnoreCase(newPassword) || "".equalsIgnoreCase(newPasswordConfirm)) {
            response.setStatus(409);
            response.setContentType("text/plain");
            response.getWriter().write("Each field should be filled");
            return;
        }
        if (!user.getPassword().equals(oldPassword)) {
            response.setStatus(409);
            response.setContentType("text/plain");
            response.getWriter().write("your old password is not correct");
            return;
        }
        if (!newPassword.equals(newPasswordConfirm)) {
            response.setStatus(409);
            response.setContentType("text/plain");
            response.getWriter().write("your confirmed password is different from new password");
            return;
        }

        user.setPassword(newPassword);

        userRepository.save(user);

        response.setStatus(200);
    }

    @PostMapping("/edit-initials")
    public ResponseEntity<Void> editInitials(@RequestBody EditInitialsDto editInitialsDto, HttpServletRequest request, HttpServletResponse response)
        throws SQLException, IOException {
        HttpSession session = request.getSession();
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            response.setStatus(401);
            return null;
        }
        User user = userRepository.findById(userId).get(); // Consider .orElseThrow() or .orElse(null) and handle null
        if (user == null) {
            response.setStatus(404); // User not found
            response.setContentType("text/plain");
            response.getWriter().write("User not found.");
            return null;
        }

        String newEmail = editInitialsDto.getEmail();
        String newUserName = editInitialsDto.getName();
        String newPhone = editInitialsDto.getPhone();

        user.setUserName(newUserName);
        user.setEmail(newEmail);
        user.setPhone(newPhone);

        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // HTTP 204 No Content
    }

    @PostMapping("/create-user")
    public void createUser(@RequestBody CreateUserDto createUserDto, HttpServletRequest request, HttpServletResponse response)
        throws IOException { // Changed SQLException to IOException for response.getWriter()
        boolean userNameExists = userRepository.existsByUserName(createUserDto.getUserName());
        boolean emailExists = userRepository.existsByEmail(createUserDto.getEmail());

        if (userNameExists) {
            response.setStatus(HttpServletResponse.SC_CONFLICT); // HTTP 409 Conflict
            response.setContentType("text/plain"); // Set content type for plain text message
            response.getWriter().write("name '" + createUserDto.getUserName() + "' is already registered so use other name.");
            return; // Stop execution here
        }

        if (emailExists) {
            response.setStatus(HttpServletResponse.SC_CONFLICT); // HTTP 409 Conflict
            response.setContentType("text/plain"); // Set content type for plain text message
            response.getWriter().write("Email '" + createUserDto.getEmail() + "' is already registered so use other Email.");
            return; // Stop execution here
        }

        // If neither username nor email exists, proceed with user creation
        User user = new User(createUserDto.getUserName(), createUserDto.getEmail(), createUserDto.getPassword(), createUserDto.getPhone());
        userRepository.save(user);
        if (user.getId() == null) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // HTTP 500 Internal Server Error
            return;
        }

        HttpSession session = request.getSession();
        session.setAttribute("userId", user.getId());
        session.setMaxInactiveInterval(3000); // Session timeout in seconds (50 minutes)
        response.setStatus(HttpServletResponse.SC_CREATED);
    }

    @DeleteMapping("/delete-user")
    public void deleteUser(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            response.setStatus(401);
            return;
        }
        User user = userRepository.findById(userId).get();
        userRepository.delete(user);
        session.invalidate();
        response.setStatus(200);
    }

    private static void setSellAdDtos(Set<Ad> ads, ProfileDto profileDto) {
        for (Ad ad : ads) {
            SellAdDto sellAdDto = new SellAdDto();

            sellAdDto.setAdId(ad.getId());
            sellAdDto.setTitle(ad.getTitle());
            sellAdDto.setDescription(ad.getDescription());
            sellAdDto.setPhotos(ad.getPhotos());
            sellAdDto.setPrice(ad.getPrice());

            TreeSet<MessageDto> messagesDtos = new TreeSet<>((o1, o2) -> o1.getDate().compareTo(o2.getDate()));
            List<Communication> communications = ad.getCommunications();

            for (int i = 0; i < communications.size(); i++) {
                Communication communication = communications.get(i);
                List<Message> messages = communication.getMessages();
                for (int j = 0; j < messages.size(); j++) {
                    MessageDto messageDto = new MessageDto();
                    Message message = messages.get(j);
                    messageDto.setDate(message.getDate());
                    messageDto.setSenderName(message.getSender().getUserName());
                    messageDto.setText(message.getText());
                    messageDto.setCommunicationId(communication.getId());
                    messageDto.setCommunicationCreatorId(communication.getCreator().getId());
                    messageDto.setCommunicationCreatorName(communication.getCreator().getUserName());

                    messagesDtos.add(messageDto);
                }
            }
            sellAdDto.setMessageDtos(messagesDtos);
            profileDto.addSellAdDto(sellAdDto);
        }
    }

    private void setBuyAdDtos(User user, ProfileDto profileDto) {
        Set<Communication> communications = new TreeSet<>(communicationRepository.findByCreator(user));
        for (Communication communication : communications) {
            Ad ad = communication.getAd();
            BuyAdDto buyAdDto = new BuyAdDto();
            TreeSet<MessageDto> messagesDtos = new TreeSet<>((o1, o2) -> o1.getDate().compareTo(o2.getDate()));
            buyAdDto.setAdId(ad.getId());
            buyAdDto.setTitle(ad.getTitle());
            buyAdDto.setDescription(ad.getDescription());
            buyAdDto.setPrice(ad.getPrice());
            List<Photo> photos = ad.getPhotos();
            buyAdDto.setPhotos(photos);
            buyAdDto.setCreator(ad.getCreator().getId());
            boolean isInFavorites = user.isItFavorite(ad);
            buyAdDto.setInFavorites(isInFavorites);

            List<Message> messages = communication.getMessages();
            for (Message message : messages) {
                MessageDto messageDto = new MessageDto();
                messageDto.setDate(message.getDate());
                messageDto.setSenderName(message.getSender().getUserName());
                messageDto.setText(message.getText());
                messageDto.setCommunicationId(communication.getId());
                messageDto.setAdCreatorId(ad.getCreator().getId());
                messageDto.setAdCreatorName(ad.getCreator().getUserName());
                messagesDtos.add(messageDto);
            }
            buyAdDto.setMessageDtos(messagesDtos);
            profileDto.addBuyAdDto(buyAdDto);
        }
    }

    @PostMapping("/add-in-favorites")
    public void addInFavorites(@RequestBody FavoriteRequestDto requestDto, HttpServletRequest request, HttpServletResponse response) {
        Long adId = requestDto.getAdId();
        HttpSession session = request.getSession();
        Long sessionUserId = (Long) session.getAttribute("userId");
        if (sessionUserId == null ) {
            response.setStatus(401);
            return;
        }
        favoriteService.markfavorite(sessionUserId, adId);
        response.setStatus(200);
    }

    @PostMapping("/remove-from-favorites")
    public void removeFromFavorites(@RequestBody FavoriteRequestDto requestDto, HttpServletRequest request, HttpServletResponse response) {
        Long adId = requestDto.getAdId();
        HttpSession session = request.getSession();
        Long sessionUserId = (Long) session.getAttribute("userId");
        if (sessionUserId == null ) {
            response.setStatus(401);
            return;
        }
        favoriteService.unmarkfavorite(sessionUserId, adId);
        response.setStatus(200);
    }
}
