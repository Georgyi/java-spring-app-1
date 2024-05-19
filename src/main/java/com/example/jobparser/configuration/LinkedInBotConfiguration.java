package com.example.jobparser.configuration;

import com.example.jobparser.telegram.LinkedInBot;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
public class LinkedInBotConfiguration {

    @Bean
    public TelegramBotsApi telegramBotsApi(LinkedInBot linkedInBot) throws TelegramApiException {
        var api = new TelegramBotsApi(DefaultBotSession.class);
        api.registerBot(linkedInBot);
        return api;
    }
}
