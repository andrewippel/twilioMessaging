package com.example.twiliomessaging.repository;

import com.example.twiliomessaging.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query(value = "SELECT * FROM messages WHERE deleted = false", nativeQuery = true)
    List<Message> findAllMessages();

    @Query(value = "SELECT * FROM messages " +
            "WHERE (:recipientNumber IS NULL OR recipient_number = :recipientNumber) " +
            "AND (:sendDateTime IS NULL OR send_date_time = :sendDateTime) AND deleted = false", nativeQuery = true)
    List<Message> searchMessages(String recipientNumber, LocalDateTime sendDateTime);
}
