package com.htm.service;

import com.htm.model.User;

public interface UserService {

	User readUser();

    User updateUser(User user);

    void deleteUser();

    User getLoggedInUser();
}
