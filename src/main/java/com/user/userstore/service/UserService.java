package com.user.userstore.service;

import com.user.userstore.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface UserService {

    User saveUser(User user);

    Optional<User> getUserByUsername(String username);

    List<User> saveUserUsingCSV(MultipartFile file) throws IOException;

}
