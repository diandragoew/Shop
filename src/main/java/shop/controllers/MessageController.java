package shop.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
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
import shop.service.CommunicationService;

import java.util.Optional;

@RestController
public class MessageController {
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private CommunicationService communicationService;

    @PostMapping("/createMessage")
    public void createMessage(@RequestParam("adId") Long adId,
                              @RequestParam("text") String text,
                              HttpServletRequest request, HttpServletResponse response) {

        HttpSession session = request.getSession();
        Long senderId = (Long) session.getAttribute("userId");

        if (senderId == null) {
            response.setStatus(401);
            return;
        }


        Long communicationId = communicationService.takeCommunicationId(senderId, adId);

        messageRepository.saveMessage(senderId, text, communicationId);

        response.setStatus(200);

    }

    @PostMapping("/addMessage")
    public void addMessage(@RequestParam("text") String text,
                           @RequestParam("communicationId") Long communicationId,
                           HttpServletRequest request, HttpServletResponse response) {

        HttpSession session = request.getSession();
        Long senderId = (Long) session.getAttribute("userId");

        if (senderId == null) {
            response.setStatus(401);
            return;
        }

        messageRepository.saveMessage(senderId,  text, communicationId);
        response.setStatus(200);

    }

}
