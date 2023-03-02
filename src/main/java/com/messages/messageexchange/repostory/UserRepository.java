package com.messages.messageexchange.repostory;

import org.springframework.data.jpa.repository.JpaRepository;

import com.messages.messageexchange.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
