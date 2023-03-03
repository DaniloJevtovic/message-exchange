package com.messages.messageexchange.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.messages.messageexchange.dto.MsgRequestDTO;
import com.messages.messageexchange.dto.MsgResponseDTO;
import com.messages.messageexchange.model.Message;
import com.messages.messageexchange.model.User;
import com.messages.messageexchange.repostory.MessageRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MessageService {

	private final MessageRepository messageRepository;
	private final UserService userService;

	public List<MsgResponseDTO> getAllMessages() {
		return messageRepository.findAll().stream().map(this::convertToMsgDTO).collect(Collectors.toList());
	}

	public Message getMessageById(Long id) {
		return messageRepository.findById(id).orElse(null);
	}

	public MsgResponseDTO sendMessage(MsgRequestDTO msgDto) {
		Message message = new Message();

		message.setSubject(msgDto.subject());
		message.setMessage(msgDto.message());
		message.setDeletedForReciver(false);
		message.setDeletedForSender(false);

		User sender = userService.getUserById(msgDto.senderId());
		User reciver = userService.getUserById(msgDto.reciverId());
		message.setSender(sender);
		message.setReciver(reciver);

		message.setDate(LocalDateTime.now());

		messageRepository.saveAndFlush(message);

		return convertToMsgDTO(message);
	}

	// potpuno brisanje poruke iz baze
	public void deleteMessage(Long id) {
		messageRepository.deleteById(id);
	}

	public List<Message> getRecivedMessagesForUser(Long userId) {
		return messageRepository.findByReciverIdAndIsDeletedForReciverFalse(userId);
	}

	public List<Message> getSentMessagesForUser(Long userId) {
		return messageRepository.findBySenderIdAndIsDeletedForSenderFalse(userId);
	}

	// primalac brise svoju primljenu poruku
	public void deleteRecivedMessage(Long msgId, Long userId) {
		Message recivedMsg = messageRepository.findByIdAndReciverId(msgId, userId);

		// ako je i posiljac obrisao poruku - obrisi je posve
		if (recivedMsg.isDeletedForSender())
			deleteMessage(msgId);
		else {
			recivedMsg.setDeletedForReciver(true);
			messageRepository.save(recivedMsg);
		}
	}

	// primalac brise svoju poslatu poruku
	public void deleteSentMessage(Long msgId, Long userId) {
		Message sentMsg = messageRepository.findByIdAndSenderId(msgId, userId);
		
		// ako je i posiljac obrisao poruku - obrisi je posve
		if(sentMsg.isDeletedForReciver())
			deleteMessage(msgId);
		else {
			sentMsg.setDeletedForSender(true);
			messageRepository.save(sentMsg);
		}
	}

	public MsgResponseDTO convertToMsgDTO(Message message) {
		return new MsgResponseDTO(message.getId(), message.getSubject(), message.getMessage(),
				message.getSender().getEmail(), message.getReciver().getEmail(), message.getDate());
	}

}