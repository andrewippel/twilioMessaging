package com.example.twiliomessaging;

import com.example.twiliomessaging.entity.Message;
import com.example.twiliomessaging.enums.EStatus;
import com.example.twiliomessaging.exception.MessageDeletedException;
import com.example.twiliomessaging.exception.MessageNotFoundException;
import com.example.twiliomessaging.repository.MessageRepository;
import com.example.twiliomessaging.service.MessageService;
import com.example.twiliomessaging.service.TwilioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MessageServiceTest {

	@Mock
	private MessageRepository messageRepository;

	@InjectMocks
	private MessageService messageService;

	@InjectMocks
	private TwilioService twilioService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void sendMessage_shouldSetStatusDelivered() {
		Message message = new Message();
		message.setSenderNumber("123");
		message.setRecipientNumber("456");
		message.setMessage("Test message");

		when(messageRepository.save(any(Message.class))).thenReturn(message);

		doNothing().when(twilioService).sendSms(anyString() ,anyString(), anyString());

		Message result = messageService.sendMessage(message);

		assertEquals(EStatus.DELIVERED, result.getStatus());
	}

	@Test
	void sendMessage_shouldSetStatusFailed() {
		Message message = new Message();
		message.setSenderNumber("123");
		message.setRecipientNumber("456");
		message.setMessage("Test message");

		when(messageRepository.save(any(Message.class))).thenReturn(message);

		doThrow(new RuntimeException("Twilio error")).when(twilioService).sendSms(anyString(), anyString(), anyString());

		Message result = messageService.sendMessage(message);

		assertEquals(EStatus.FAILED, result.getStatus());
	}

	@Test
	void getAllMessages_shouldReturnAllMessages() {
		Message message1 = new Message();
		message1.setId(1L);
		Message message2 = new Message();
		message2.setId(2L);

		when(messageRepository.findAllMessages()).thenReturn(Arrays.asList(message1, message2));

		List<Message> messages = messageService.getAllMessages();

		assertEquals(2, messages.size());
		verify(messageRepository, times(1)).findAllMessages();
	}

	@Test
	void getMessageById_shouldReturnMessageWithId() {
		Message message = new Message();
		message.setId(1L);

		when(messageRepository.findById(1L)).thenReturn(Optional.of(message));

		Message foundMessage = messageService.getMessageById(1L);

		assertNotNull(foundMessage);
		assertEquals(1L, foundMessage.getId());
		verify(messageRepository, times(1)).findById(1L);
	}

	@Test
	void getMessageById_shouldThrowMessageNotFoundException() {
		when(messageRepository.findById(1L)).thenReturn(Optional.empty());

		MessageNotFoundException exception = assertThrows(MessageNotFoundException.class, () -> {
			messageService.getMessageById(1L);
		});

		assertEquals("Message not found.", exception.getMessage());
		verify(messageRepository, times(1)).findById(1L);
	}

	@Test
	void getMessageById_shouldThrowMessageDeletedException() {
		Message message = new Message();
		message.setId(1L);
		message.setDeleted(true);

		when(messageRepository.findById(1L)).thenReturn(Optional.of(message));

		MessageDeletedException exception = assertThrows(MessageDeletedException.class, () -> {
			messageService.getMessageById(1L);
		});

		assertEquals("This message has been deleted.", exception.getMessage());
		verify(messageRepository, times(1)).findById(1L);
	}

	@Test
	void deleteMessage_shouldMarkMessageAsDeleted() {
		Message message = new Message();
		message.setId(1L);
		message.setDeleted(false);

		when(messageRepository.findById(1L)).thenReturn(Optional.of(message));
		when(messageRepository.save(any(Message.class))).thenReturn(message);

		messageService.deleteMessage(1L);

		assertTrue(message.getDeleted());
		verify(messageRepository, times(1)).findById(1L);
		verify(messageRepository, times(1)).save(message);
	}

	@Test
	void deleteMessage_shouldThrowMessageDeletedException() {
		Message message = new Message();
		message.setId(1L);
		message.setDeleted(true);

		when(messageRepository.findById(1L)).thenReturn(Optional.of(message));

		MessageDeletedException exception = assertThrows(MessageDeletedException.class, () -> {
			messageService.deleteMessage(1L);
		});

		assertEquals("This message has already been deleted.", exception.getMessage());
		verify(messageRepository, times(1)).findById(1L);
	}
}
