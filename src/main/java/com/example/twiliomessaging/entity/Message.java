package com.example.twiliomessaging.entity;

import com.example.twiliomessaging.enums.EStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String senderNumber;

    @Column(nullable = false)
    private String recipientNumber;

    @Column(name = "message", nullable = false)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EStatus status;

    @Column(nullable = false)
    private Boolean deleted = false;

    @Column(name = "send_date_time", nullable = false)
    private LocalDateTime sendDateTime;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getSenderNumber() {
        return senderNumber;
    }
    public void setSenderNumber(String senderNumber) {
        this.senderNumber = senderNumber;
    }

    public String getRecipientNumber() {
        return recipientNumber;
    }
    public void setRecipientNumber(String recipientNumber) {
        this.recipientNumber = recipientNumber;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public EStatus getStatus() {
        return status;
    }
    public void setStatus(EStatus status) {
        this.status = status;
    }

    public Boolean getDeleted() {
        return deleted;
    }
    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public LocalDateTime getSendDateTime() {
        return sendDateTime;
    }

    public void setSendDateTime(LocalDateTime sendDateTime) {
        this.sendDateTime = sendDateTime;
    }

}
