package com.user.userstore.service;

import com.user.userstore.model.User;
import com.user.userstore.repository.UserRepository;
import com.user.userstore.utility.CsvUserParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CsvUserParser csvUserParser;

    @Override
    public User saveUser(User user) {
        log.info("Saving user with username: {}", user.getUsername());
        User savedUser = userRepository.save(user);
        log.info("User saved successfully: {}", savedUser.getUsername());
        return savedUser;
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        log.info("Fetching user with username: {}", username);
        return userRepository.findById(username);
    }

    @Override
    public List<User> saveUserUsingCSV(MultipartFile file) throws IOException {
        String csvContent = new String(file.getBytes());
        List<User> users = csvUserParser.parse(csvContent);

        if (users.isEmpty())
            throw new IllegalArgumentException("CSV contains no valid user data");

        return users.stream().map(this::saveUser).toList();
    }

}
