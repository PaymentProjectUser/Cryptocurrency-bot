package com.course.change.demo;

import com.course.change.demo.service.impl.CourseChangeBotBotImplService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
@RequiredArgsConstructor
public class CourseChangeApplication {

	@Autowired
	static CourseChangeBotBotImplService courseChangeBotClass;

	private static final Logger log = LoggerFactory.getLogger(CourseChangeApplication.class);

	public static void main(String[] args) throws TelegramApiException {
		TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
		try {
			log.info("Registering bot...");
			telegramBotsApi.registerBot(courseChangeBotClass);
		} catch (TelegramApiRequestException exception) {
			log.error("Failed to register bot(check internet connection / bot token or " +
					"make sure only one instance of bot is running).", exception);
		}
		log.info("Telegram bot is ready to accept updates from user......");
	}

}
