package com.example.jobparser.database.service;

import com.example.jobparser.database.entity.User;
import com.example.jobparser.database.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<User> findUsers() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public boolean checkRegisterUser(Long id) {
        return userRepository.existsUserByTelegramId(id);
    }

    @Transactional(readOnly = true)
    public Optional<User> findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    public Optional<User> register(User user) {
        System.out.println("Register user from telegram: " + user);

        var isExist = userRepository.existsUserByTelegramId(user.getTelegramId());

        if (isExist) {
            return Optional.empty();
        }

        return Optional.of(userRepository.save(user));
    }
}
