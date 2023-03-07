package com.messages.messageexchange.dto;

import java.time.LocalDateTime;

public record MsgResponseDTO(Long id, String subject, String message, String sender, String reciver, Boolean isRead,
		LocalDateTime date) {

}
