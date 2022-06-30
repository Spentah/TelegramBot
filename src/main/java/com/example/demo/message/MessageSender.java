package com.example.demo.message;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class MessageSender {

    public static SendMessage sendMessage(String chatId, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableHtml(true);
//        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        return sendMessage;
    }

}
