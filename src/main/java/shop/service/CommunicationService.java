package shop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shop.dao.CommunicationRepository;
import shop.dao.MessageRepository;
import shop.model.Communication;
import shop.model.Message;

@Service
public class CommunicationService {
    @Autowired
    MessageRepository messageRepository;

    @Autowired
    CommunicationRepository communicationRepository;

    public Long takeCommunicationId(Long senderId, Long adId) {

        Message message = messageRepository.takeMessage(senderId, adId);
        Long communicationId = null;
        if (message != null && message.getCommunication().getId() != null && message.getCommunication().getId() != 0) {
            communicationId = message.getCommunication().getId();
        } else {
            Communication communication = new Communication();
            communicationRepository.save(communication);
            communicationId = communication.getId();
        }
        return communicationId;
    }
}