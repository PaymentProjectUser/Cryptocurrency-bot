package com.course.change.demo.service.impl;

import com.course.change.demo.dto.CourseChangeResponseDto;
import com.course.change.demo.exception.TelegramBotNotAvailableToAddNewUserException;
import com.course.change.demo.service.CourseChangeBotService;
import com.course.change.demo.service.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Slf4j
@RequiredArgsConstructor
@Service
public class CourseChangeBotBotImplService extends TelegramLongPollingBot implements CourseChangeBotService {

    private final UserService userService;

//  @Value("${telegram.security.bot-name}") //TODO
    private static final String botName = "Course_change_test_bot";

    private static final String token = "6695514972:AAHmqs8wbpFJHVZi6q_YHW8EKuMBXsagalE";

    @Override
    public void onUpdateReceived(Update update) {
        log.debug("Receive new Update. updateID: " + update.getUpdateId());

        Long chatId = update.getMessage().getChatId();
        String inputText = update.getMessage().getText();

        if (inputText.startsWith("/start")) {

            if (userService.isMaximunNumberOfUsersReached()) {
                throw new TelegramBotNotAvailableToAddNewUserException();
            }

            userService.addUser(update.getMessage().getChat().getUserName(),
                    chatId);

            SendMessage message = new SendMessage();
            message.setChatId(String.valueOf(chatId));
            message.setText("Hello. This is start message");

            try {
                execute(message);
            } catch (TelegramApiException exception) {
                exception.printStackTrace(); //TODO refactor
            }
        }
    }

    public void sendMessages(List<Long> chatIds, CourseChangeResponseDto courseChangeResponseDto) {
        for (Long chatId : chatIds) {
            SendMessage message = new SendMessage();
            message.setChatId(String.valueOf(chatId));
            StringBuilder stringMessage = new StringBuilder();

            for (Map.Entry<String, Double> entry :
                    courseChangeResponseDto.getChangedCurrencies().entrySet()) {
                stringMessage.append("Symbol - ").append(entry.getValue())
                        .append(", price - ").append(entry.getKey()).append("\n");
            }

            for (Map.Entry<String, Double> entry :
                    courseChangeResponseDto.getNewCurrencies().entrySet()) {
                stringMessage.append("Symbol - ").append(entry.getValue())
                        .append(", price - ").append(entry.getKey()).append("\n");
            }

            message.setText(stringMessage.toString());
            try {
                execute(message);
            } catch (TelegramApiException exception) {
                exception.printStackTrace(); //TODO refactor
            }
        }
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return token;
    }
}
