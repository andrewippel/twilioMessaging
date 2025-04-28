package com.example.twiliomessaging.service;

import com.example.twiliomessaging.entity.Message;
import com.example.twiliomessaging.enums.EStatus;
import com.example.twiliomessaging.exception.MessageDeletedException;
import com.example.twiliomessaging.exception.MessageFailedException;
import com.example.twiliomessaging.exception.MessageNotFoundException;
import com.example.twiliomessaging.repository.MessageRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {

    private static final Logger logger = LogManager.getLogger(MessageService.class);
    private final MessageRepository messageRepository;
    private final TwilioService twilioService;

    public MessageService(MessageRepository messageRepository, TwilioService twilioService) {
        this.messageRepository = messageRepository;
        this.twilioService = twilioService;
    }

    public Message sendMessage(Message message) {
        message.setSendDateTime(LocalDateTime.now());
        message.setStatus(EStatus.SENT);
        Message savedMessage = messageRepository.save(message);
        logger.info("Message saved with ID: {}", savedMessage.getId());

        try {
            twilioService.sendSms(savedMessage.getSenderNumber(), savedMessage.getRecipientNumber(), savedMessage.getMessage());
            savedMessage.setStatus(EStatus.DELIVERED);
            logger.info("Message sent successfully via Twilio");
        } catch (Exception e) {
            savedMessage.setStatus(EStatus.FAILED);
            messageRepository.save(savedMessage);
            logger.error("Failed to send message via Twilio. Error: {}", e.getMessage());
            throw new MessageFailedException("Failed to send SMS via Twilio.", e);
        }

        return messageRepository.save(savedMessage);
    }

    public List<Message> getAllMessages() {
        logger.info("Fetching all messages");

        return messageRepository.findAllMessages();
    }

    public Message getMessageById(Long id) {
        logger.info("Fetching message with ID: {}", id);

        Message message = messageRepository.findById(id).orElseThrow(() -> new MessageNotFoundException("Message not found."));
        if (Boolean.TRUE.equals(message.getDeleted())) {
            throw new MessageDeletedException("This message has been deleted.");
        }

        return message;
    }

    public void deleteMessage(Long id) {
        logger.info("Trying to delete message with ID: {}", id);

        Message message = messageRepository.findById(id).orElseThrow(() -> new MessageNotFoundException("Message not found."));
        if (Boolean.TRUE.equals(message.getDeleted())) {
            throw new MessageDeletedException("This message has already been deleted.");
        }

        message.setDeleted(true);
        messageRepository.save(message);
        logger.info("Message deleted with ID: {}", id);
    }

    public List<Message> searchMessages(String recipientNumber, LocalDateTime sendDateTime) {
        logger.info("Searching messages with filters RecipientNumber: {} and SendDateTime: {}", recipientNumber, sendDateTime);

        // It's necessary to encode the number, because the URL treats the "+" sign as a space
        String recipientNumberEncoded = URLEncoder.encode(recipientNumber, StandardCharsets.UTF_8);
        return messageRepository.searchMessages(recipientNumberEncoded, sendDateTime);
    }
}
