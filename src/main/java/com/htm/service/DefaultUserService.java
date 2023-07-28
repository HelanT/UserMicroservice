package com.htm.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.htm.model.User;
import com.htm.model.UserDTO;

public interface DefaultUserService extends UserDetailsService{
	User save(UserDTO userRegisteredDTO);

}
