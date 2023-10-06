package com.course.change.demo.service.impl;

import com.course.change.demo.model.User;
import com.course.change.demo.repository.UserRepository;
import com.course.change.demo.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public void addUser(String userName, Long chatId) {
        userRepository.save(User.builder()
                .userName(userName)
                .chatId(chatId)
                .build()); //TODO
    }

    @Override
    public long allUsersCount() {
        return userRepository.count();
    }

    @Override
    public boolean isMaximunNumberOfUsersReached() {
        return allUsersCount() == 50; //TODO
    }

    @Override
    public List<Long> getAllChatIds() {
        return userRepository.findAll()
                .stream()
                .map(User::getChatId)
                .toList();
    }
}
