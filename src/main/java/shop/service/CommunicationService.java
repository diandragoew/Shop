package shop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shop.dao.AdRepository;
import shop.dao.CommunicationRepository;
import shop.dao.MessageRepository;
import shop.dao.UserRepository;
import shop.model.Ad;
import shop.model.Communication;
import shop.model.Message;
import shop.model.User;

@Service
public class CommunicationService {
    @Autowired
    MessageRepository messageRepository;

    @Autowired
    AdRepository adRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CommunicationRepository communicationRepository;

    public Long takeCommunicationId(Long senderId, Long adId) {

        User sender = userRepository.findById(senderId).get();
        Ad ad = adRepository.findById(adId).get();

        Communication communication = communicationRepository.findByCreatorAndAd(sender, ad);
        Long communicationId;
        if (communication != null && communication.getId() != null && communication.getId() != 0) {
            communicationId = communication.getId();
        } else {
            communication = new Communication();
            communication.setCreator(sender);
            communication.setAd(ad);
            communicationRepository.save(communication);
            communicationId = communication.getId();
        }
        return communicationId;
    }
}