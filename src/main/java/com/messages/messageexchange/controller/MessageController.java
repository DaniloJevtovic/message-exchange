package com.messages.messageexchange.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.messages.messageexchange.dto.MsgRequestDTO;
import com.messages.messageexchange.dto.MsgResponseDTO;
import com.messages.messageexchange.model.Message;
import com.messages.messageexchange.service.MessageService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("api/messages")
@AllArgsConstructor
public class MessageController {

	private final MessageService messageService;

	@GetMapping
	public List<MsgResponseDTO> getAllMessages() {
		return messageService.getAllMessages();
	}

	// dobavljanje poslate poruke za posiljaoca
	@GetMapping("/{id}")
	public Message getMessageById(@PathVariable Long id) {
		return messageService.getMessageById(id);
	}

	// citanje poruke - primalac i oznacavanje porike kao procitane
	@GetMapping("/read/{msgId}/reciver/{recId}")
	public Message readMessage(@PathVariable Long msgId, @PathVariable Long recId) {
		return messageService.readMessage(msgId, recId);
	}

	@GetMapping("/recived/{id}")
	public List<Message> getRecivedMessagesForUser(@PathVariable Long id) {
		return messageService.getRecivedMessagesForUser(id);
	}

	@GetMapping("/sent/{id}")
	public List<Message> getSentMessagesForUser(@PathVariable Long id) {
		return messageService.getSentMessagesForUser(id);
	}

	@GetMapping("/allMessages/user/{userId}")
	public List<Message> getAllMessagesForUser(@PathVariable Long userId) {
		return messageService.getAllMessagesForUser(userId);
	}

	@GetMapping("/search/{subject}/user/{userId}")
	public List<Message> search(@PathVariable String subject, @PathVariable Long userId) {
		return messageService.searchMessages(subject, userId);
	}

	@PostMapping
	public MsgResponseDTO sendMessage(@RequestBody MsgRequestDTO msgRequestDTO) {
		return messageService.sendMessage(msgRequestDTO);
	}

	// primalac moze da oznaci poruku kao procitanu
	@PatchMapping("/markAsRead/{msgId}")
	public Message markAsRead(@PathVariable Long msgId) {
		return messageService.markAsRead(msgId);
	}

	@DeleteMapping("/{id}")
	public void deleteMessage(@PathVariable Long id) {
		messageService.deleteMessage(id);
	}

	@DeleteMapping("/recived/{msgId}/user/{userId}")
	public void deleteRecivedMessage(@PathVariable Long msgId, @PathVariable Long userId) {
		messageService.deleteRecivedMessage(msgId, userId);
	}

	@DeleteMapping("/sent/{msgId}/user/{userId}")
	public void deleteSentMessage(@PathVariable Long msgId, @PathVariable Long userId) {
		messageService.deleteSentMessage(msgId, userId);
	}

	@DeleteMapping("/allSent/user/{userId}")
	public void deleteAllSentMessages(@PathVariable Long userId) {
		messageService.deleteAllSentMessages(userId);
	}

	@DeleteMapping("/allRecived/user/{userId}")
	public void deleteAllRecivedMessages(@PathVariable Long userId) {
		messageService.deleteAllRecivedMessages(userId);
	}

}
