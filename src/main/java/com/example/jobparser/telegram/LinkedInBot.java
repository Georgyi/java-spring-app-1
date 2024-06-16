package com.example.jobparser.telegram;

import com.example.jobparser.database.entity.User;
import com.example.jobparser.database.service.ParseQueryService;
import com.example.jobparser.database.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@Slf4j
public class LinkedInBot extends TelegramLongPollingBot {
    private static final String HELP = "/help";
    private static final String REGISTER = "/register";
    private static final String ADMIN_HELP = "/admin__help";
    private static final String ADMIN_GET_PARSE_QUERY = "/admin__get_parse_query";
    private static final String ADMIN_CLEAR_PARSE_PARAM = "/admin__clear_parse_param";

    @Value("${telegram.bot.username}")
    private String botName;

    @Value("${telegram.chat.id}")
    private Long linkedInChatId;

    private final UserService userService;
    private final ParseQueryService parseQueryService;

    @Autowired
    public LinkedInBot(
            @Value("${telegram.bot.api-key}") String botToken,
            UserService userService,
            ParseQueryService parseQueryService
    ) {
        super(botToken);
        this.userService = userService;
        this.parseQueryService = parseQueryService;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return;
        }

        var chatId = update.getMessage().getChatId();
        var message = update.getMessage().getText();

        log.info("                                                                                                            TELEGRAM");

        // Общий чат
        if (chatId.toString().equals(linkedInChatId.toString())) {
            return;
        }

        if (message.contains("/admin__")) {
            var userId = update.getMessage().getFrom().getId();

            var isRegister = userService.checkRegisterUser(userId);

            if (!isRegister) {
                sendMessage(chatId, "Ваша команда была отклонена. Вы не имеета прав доступа к ним");
                return;
            }
        }


        // Личное сообщение боту
        if (message.equals(HELP)) {
            helpCommand(chatId);
        } else if (message.equals(REGISTER)) {
            register(update);
        } else if (message.contains(ADMIN_HELP)) {
            helpAdminCommand(chatId);
        } else if (message.equals(ADMIN_GET_PARSE_QUERY)) {
            getParseQuery(chatId);
        } else if (message.contains(ADMIN_CLEAR_PARSE_PARAM)) {
            clearParseParam(message, chatId);
        } else {
            unknownCommand(chatId);
        }
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    public void sendMessageToPrivateChat(String message) {
        sendMessage(linkedInChatId, message);
    }

    private void sendMessage(Long chatId, String message) {
        var stringChatId = String.valueOf(chatId);
        var sm = new SendMessage(stringChatId, message);

        sm.enableMarkdown(true);

        try {
            execute(sm);
        } catch (TelegramApiException e) {
            System.out.println("After send message was wrong :(");

            throw new RuntimeException(e);
        }
    }

    private void getParseQuery(Long chatId) {
        var parseQueryOptional = parseQueryService.getParseQuery();

        if (parseQueryOptional.isPresent()) {
            var queryParse = parseQueryOptional.get();

            var text = """
                    Параметры парсинга вакансий:
                                        
                    *id:* %s
                    *f_T:* %s
                    *geo_id:* %s
                    *keywords:* %s
                    *location:* %s
                    *origin:* %s
                    *sort_by:* %s
                                        
                    Последний раз обновлялся: %s
                    """.formatted(
                    queryParse.getId(),
                    queryParse.getFT(),
                    queryParse.getGeoId(),
                    queryParse.getKeywords(),
                    queryParse.getLocation(),
                    queryParse.getOrigin(),
                    queryParse.getSortBy(),
                    queryParse.getUpdatedAt()
            );

            sendMessage(chatId, text);
        }
    }

    private void clearParseParam(String message, Long chatId) {
        var splitResult = message.split(" ");

        if (splitResult.length != 2) {
            sendMessage(chatId, "Передано не правильное количество параметров для очистки параметра парсинга");
            return;
        }

        var param = splitResult[1];

        if (param.isEmpty()) {
            sendMessage(chatId, "Передан не правильный ключ: '" + param + "'");
        }

        parseQueryService.clearQueryParam(param);

        sendMessage(chatId, "Поле: " + param + " было успешно очищено");
    }

    private void register(Update update) {
        var userId = update.getMessage().getFrom().getId();
        var userName = update.getMessage().getFrom().getUserName();
        var firstName = update.getMessage().getFrom().getFirstName();
        var lastName = update.getMessage().getFrom().getLastName();
        var chatId = update.getMessage().getChatId();

        var user = User.builder()
                .telegramId(userId)
                .userName(userName)
                .firstName(firstName)
                .lastName(lastName)
                .build();

        var result = userService.register(user);

        if (result.isPresent()) {
            sendMessage(chatId, "Регистрация прошла успешно!");
        } else {
            sendMessage(chatId, "Вы уже зарегистрированы!");
        }
    }

    private void helpCommand(Long chatId) {
        var text = """
                Команды:
                                
                *%s* - посмотреть команды не авторизированного пользователя
                *%s* - регистрация в приложении
                *%s* - посмотреть команды админа
                """.formatted(HELP, REGISTER, ADMIN_HELP);
        sendMessage(chatId, text);
    }

    private void helpAdminCommand(Long chatId) {
        var text = """
                Все команды:
                                
                *%s* - посмотреть команды не авторизированного пользователя
                *%s* - регистрация в приложении
                *%s* - посмотреть команды админа
                *%s* - получить параметры парсинга
                *%s* - очистить параметр для парсинга
                """.formatted(HELP, REGISTER, ADMIN_HELP, ADMIN_GET_PARSE_QUERY, ADMIN_CLEAR_PARSE_PARAM);
        sendMessage(chatId, text);
    }

    private void unknownCommand(Long chatId) {
        var text = "Самый умный? Хочешь угадать секретную команду чтобы получить ко мне полный доступ? Удачи ;D";
        sendMessage(chatId, text);
    }
}
