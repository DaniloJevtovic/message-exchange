package com.messages.messageexchange.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "messages")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String subject;
	private String message;
	
	@ManyToOne
	private User sender;
	
	@ManyToOne
	private User reciver;
	
	private boolean isDeletedForSender;
	private boolean isDeletedForReciver;
	private boolean isRead;
	
	@JsonFormat(pattern="dd-MM-yyyy / HH:mm:ss", timezone="Europe/Belgrade")
	private LocalDateTime date;
}
