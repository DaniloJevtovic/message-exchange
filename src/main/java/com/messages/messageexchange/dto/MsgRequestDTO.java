package com.messages.messageexchange.dto;

public record MsgRequestDTO(String subject, String message, Long senderId, Long reciverId) {

}
