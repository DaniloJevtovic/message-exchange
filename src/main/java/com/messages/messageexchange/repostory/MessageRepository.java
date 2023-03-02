package com.messages.messageexchange.repostory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.messages.messageexchange.model.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {

	// primljene poruke (koje nisu obrisane)
	List<Message> findByReciverIdAndIsDeletedForReciverFalse(Long userId);

	// poslate poruke (koje nisu obrisane)
	List<Message> findBySenderIdAndIsDeletedForSenderFalse(Long userId);

	// primljena poruka
	Message findByIdAndReciverId(Long msgId, Long userId);

	// poslata poruka
	Message findByIdAndSenderId(Long msgId, Long userId);

}
