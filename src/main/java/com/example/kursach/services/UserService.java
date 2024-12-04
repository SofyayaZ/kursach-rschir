package com.example.kursach.services;

import com.example.kursach.models.User;
import com.example.kursach.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User findUserById(Long userId) {
        Optional<User> userFromDB = userRepository.findById(userId);
        return userFromDB.orElse(new User());
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean existsByUsername(String username) {
        return allUsers().stream().anyMatch(user -> user.getUsername().equals(username));
    }

    public List<User> allUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public void saveUser(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        userRepository.save(user);
    }
}
