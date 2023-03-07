package com.messages.messageexchange.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public record MsgResponseDTO(
		Long id, 
		String subject, 
		String message, 
		String sender, 
		String reciver, 
		Boolean isRead,
		@JsonFormat(pattern = "dd.MM.yyyy / HH:mm:ss", timezone = "Europe/Belgrade") 
		LocalDateTime date
) {
}
