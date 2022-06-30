package com.example.demo;

import com.example.demo.message.MessageBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static com.example.demo.message.MessageSender.sendMessage;
import static com.example.demo.utils.Utils.isMessageEquals;

@Component
public class MyBot extends TelegramLongPollingBot {

    private static final String BOT_USERNAME = "LaunchStatBot";
    private static final String BOT_TOKEN = "5294263061:AAEyO3ejpDsqiVR9w5ISV9v0RQqR-_P-l8A";
    private String launchId;

//    @Autowired
//    public RequestSender requestSender;

    @Autowired
    public MessageBuilder messageBuilder;

    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    public void executeMessage(Update update, String text) {
        try {
            execute(sendMessage(update.getMessage().getChatId().toString(), text));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    //TODO обавить try-catch, обработчики сообщений, добавить ожидание ответа, добавить, добавить вывод айдишника
    public void onUpdateReceived(Update update) {
        if (isMessageEquals(update, "/start")) {
            executeMessage(update, "Введи id прогона");
        } else if (update.getMessage().getText().matches("[#]?\\d+")) {
            String text = update.getMessage().getText().replaceAll("#", "");
            // проверяем, что айдишник валидный
            if (text.length() != 4) {
                executeMessage(update, "ID прогона должен состоять из 4 цифр");
            } else {
                launchId = text;
//                String statistic = allureStatistic.getLaunchStatistic(launchId) + allureStatistic.getEnvironment(launchId);
//                executeMessage(update, requestSender.getLaunchStatistic(launchId));
                executeMessage(update, messageBuilder.getLaunchStatistic(launchId));
            }
        } else {
            executeMessage(update, "Не понял твою команду");
        }
    }
}
