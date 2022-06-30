package com.example.demo.message;

import com.example.demo.api.RequestSender;
import com.example.demo.parser.ResponseParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageBuilder {

    @Autowired
    private ResponseParser parser;

    @Autowired
    private RequestSender request;

    private String setMessageHeader(String launchId) {
        return "<b>LAUNCH ID: " + launchId + "</b>\n\n";
    }

    public String getLaunchStatistic(String launchId) {
        StringBuilder builder = new StringBuilder(setMessageHeader(launchId));
        builder.append(parser.getLaunchEnvironment(request.getEnvironment(launchId)));
        builder.append("\n");
        builder.append(parser.getLaunchStatistic(request.getLaunchStatistic(launchId)));
        builder.append("\n");
        builder.append(parser.getTestResult(request.getTestResult(launchId)));
        return builder.toString();
    }
}
