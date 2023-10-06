package com.course.change.demo.service;

import java.util.List;

public interface UserService {
    void addUser(String userName, Long chatId);

    long allUsersCount();

    boolean isMaximunNumberOfUsersReached();

    List<Long> getAllChatIds();
}
