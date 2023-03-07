package com.messages.messageexchange.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

	// ovo moze i na frontendu da se provjeri
	public Message readMessage(Long msgId, Long reciverId) {
		Message message = getMessageById(msgId);

		if (message.getReciver().getId().equals(reciverId) && !message.isRead()) {
			message.setRead(true);
			return messageRepository.save(message);
		}
		
		return message;
	}

	// potpuno brisanje poruke iz baze
	public void deleteMessage(Long id) {
		messageRepository.deleteById(id);
	}

	public List<Message> getRecivedMessagesForUser(Long userId) {
		return messageRepository.findByReciverIdAndIsDeletedForReciverFalseOrderByDateDesc(userId);
	}

	public List<Message> getSentMessagesForUser(Long userId) {
		return messageRepository.findBySenderIdAndIsDeletedForSenderFalseOrderByDateDesc(userId);
	}

	public List<Message> getAllMessagesForUser(Long userId) {
		return Stream.concat(getRecivedMessagesForUser(userId).stream(), getSentMessagesForUser(userId).stream())
				.collect(Collectors.toList());
	}

	public List<Message> searchMessages(String subject, Long userId) {
		return getAllMessagesForUser(userId).stream().filter(msg -> msg.getSubject().contains(subject))
				.collect(Collectors.toList());
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
		if (sentMsg.isDeletedForReciver())
			deleteMessage(msgId);
		else {
			sentMsg.setDeletedForSender(true);
			messageRepository.save(sentMsg);
		}
	}

	public void deleteAllRecivedMessages(Long userId) {
		getRecivedMessagesForUser(userId).forEach(m -> {
			if (m.isDeletedForSender())
				deleteMessage(m.getId());
			else {
				m.setDeletedForReciver(true);
				messageRepository.save(m);
			}
		});
	}

	public void deleteAllSentMessages(Long userId) {
		getSentMessagesForUser(userId).forEach(m -> {
			if (m.isDeletedForReciver())
				deleteMessage(m.getId());
			else {
				m.setDeletedForSender(true);
				messageRepository.save(m);
			}
		});
	}

	public void setAsRead(Long msgId) {
		Message message = getMessageById(msgId);
		message.setRead(true);
		messageRepository.save(message);
	}

	public MsgResponseDTO convertToMsgDTO(Message message) {
		return new MsgResponseDTO(message.getId(), message.getSubject(), message.getMessage(),
				message.getSender().getEmail(), message.getReciver().getEmail(), message.getDate());
	}

}
