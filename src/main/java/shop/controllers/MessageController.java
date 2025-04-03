package shop.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import shop.dao.AdRepository;
import shop.dao.MessageRepository;
import shop.dao.UserRepository;
import shop.model.Ad;
import shop.model.Message;
import shop.model.User;

import java.util.Optional;

@RestController
public class MessageController {
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdRepository adRepository;
    @PostMapping("/createMessage")
    public void createMessage(@RequestParam("adId") Long adId,
                              @RequestParam("text") String text,
                              HttpServletRequest request, HttpServletResponse response ) {

        HttpSession session = request.getSession();
        Long userId = (Long) session.getAttribute("userId");

        if(userId == null) {
            response.setStatus(401);
            return;
        }


        messageRepository.saveMessage(userId, adId, text);

        response.setStatus(200);

    }
}
