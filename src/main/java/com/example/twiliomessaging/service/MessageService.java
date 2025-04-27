package com.example.twiliomessaging.service;

import com.example.twiliomessaging.entity.Message;
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

    public Message createMessage(Message message) {
        message.setStatus("sent");
        Message saved = messageRepository.save(message);
        logger.info("Message created: " + saved.getId());
        return saved;
    }

    public List<Message> getAllMessages() {
        logger.info("Fetching all messages");
        return messageRepository.findAllActiveMessages();
    }

    public Message getMessageById(Long id) {
        logger.info("Fetching message with ID: " + id);
        return messageRepository.findActiveMessageById(id)
                .orElseThrow(() -> new RuntimeException("Message not found"));
    }

    public void deleteMessage(Long id) {
        Message message = getMessageById(id);
        message.setDeleted(true);
        messageRepository.save(message);
        logger.info("Message logically deleted with ID: " + id);
    }
}
