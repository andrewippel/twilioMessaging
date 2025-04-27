package com.example.twiliomessaging.service;

import com.example.twiliomessaging.entity.Message;
import com.example.twiliomessaging.enums.EStatus;
import com.example.twiliomessaging.repository.MessageRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    private static final Logger logger = LogManager.getLogger(MessageService.class);
    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Message sendMessage(Message message) {
        message.setStatus(EStatus.SENT);
        Message savedMessage = messageRepository.save(message);
        logger.info("Message sent: {}", savedMessage.getId());
        return savedMessage;
    }

    public List<Message> getAllMessages() {
        logger.info("Fetching all messages");
        return messageRepository.findAllMessages();
    }

    public Message getMessageById(Long id) {
        logger.info("Fetching message with ID: {}", id);
        return messageRepository.findMessageById(id)
                .orElseThrow(() -> new RuntimeException("Message not found"));
    }

    public void deleteMessage(Long id) {
        Message message = getMessageById(id);
        message.setDeleted(true);
        messageRepository.save(message);
        logger.info("Message deleted with ID: {}", id);
    }
}
