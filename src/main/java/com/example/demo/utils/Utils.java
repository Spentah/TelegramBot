package com.example.demo.utils;

import org.telegram.telegrambots.meta.api.objects.Update;


public class Utils {

    public static boolean isMessageEquals(Update update, String text) {
        return update.hasMessage() && update.getMessage().getText().equals(text);
    }

}
