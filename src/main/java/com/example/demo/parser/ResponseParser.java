package com.example.demo.parser;

import com.example.demo.ContextHolder;
import com.example.demo.api.RequestSender;
import com.example.demo.pojo.LaunchEnvironment;
import com.example.demo.pojo.LaunchStatistic;
import com.example.demo.pojo.TestCaseMember;
import com.example.demo.pojo.TestResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ResponseParser {

    @Autowired
    private RequestSender requestSender;

    public String getLaunchStatistic(String response) {
        List<LaunchStatistic> statistic = null;
        try {
            statistic = new ObjectMapper().readValue(response, new TypeReference<List<LaunchStatistic>>() {
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        int total = statistic.stream()
                .mapToInt(LaunchStatistic::getCount)
                .sum();
        int passed = statistic.stream().filter(e -> e.getStatus().equals("passed"))
                .mapToInt(LaunchStatistic::getCount).sum();
        String percent = new DecimalFormat("##.##").format(passed / (total / 100.0));

        StringBuilder builder = new StringBuilder();
        builder.append("ВСЕГО ТЕСТОВ: " + total + "\n");
        builder.append("<b>УСПЕШНОСТЬ ЗАПУСКА - ").append(percent).append("%</b>");
        builder.append("\n");

        for (LaunchStatistic var : statistic) {
            String status = var.getStatus().toUpperCase();
            int count = var.getCount();
            builder.append(status).append(": ").append(count).append("\n");
        }
        return builder.toString();
    }

    public String getLaunchEnvironment(String response) {
        List<LaunchEnvironment> environment = null;
        try {
            environment = new ObjectMapper().readValue(response, new TypeReference<List<LaunchEnvironment>>() {
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        StringBuilder builder = new StringBuilder();
        builder.append("ENVIRONMENT:\n");

        for (LaunchEnvironment var : environment) {
            String name = var.getVariable().getName();
            String value = var.getName();
            builder.append(name).append(": ").append(value).append("\n");
        }
        return builder.toString();
    }

    public String getTestcaseMemberById(String response) {
        List<TestCaseMember> members = null;
        try {
            members = new ObjectMapper().readValue(response, new TypeReference<List<TestCaseMember>>() {
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        Optional<TestCaseMember> member = members.stream()
                .filter(e -> e.getRole().getName().equals("AT author"))
                .findFirst();
        if (!member.isPresent()) {
            member = members.stream().filter(e -> e.getRole().getName().equals("Owner"))
                    .findFirst();
        }
        String memberName = member.get().getName();
        return memberName;
    }


    public String getTestResult(String response) {
        TestResult testResult = null;
        try {
            testResult = new ObjectMapper().readValue(response, TestResult.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        StringBuilder builder = new StringBuilder();
        List<TestResult.ContentItem> failedTests = testResult.getContent().stream().filter(e -> e.getStatus().equals("failed"))
                .collect(Collectors.toList());
        List<TestResult.ContentItem> passedTests = testResult.getContent().stream().filter(e -> e.getStatus().equals("passed"))
                .collect(Collectors.toList());

        Map<String, Integer> allCount = new HashMap<>();
        Map<String, Integer> failedCount = new HashMap<>();
        Map<String, Integer> passedCount = new HashMap<>();
        int totalTests = 0;

        //проход по всем тестам, запись общего кол-ва тестов по каждому автору
        for (TestResult.ContentItem item : testResult.getContent()) {
            String testCaseId = String.valueOf(item.getTestCaseId());
            if (ContextHolder.getValue(testCaseId) == null) {
                String member = getTestcaseMemberById(requestSender.getTestCaseMember(testCaseId));
                ContextHolder.put(testCaseId, member);
            }
            String member = ContextHolder.getValue(testCaseId);
            allCount.merge(member, 1, Integer::sum);
            totalTests++;
        }

        //проход только по упавшим тестам, запись по каждому автору
        for (TestResult.ContentItem item : failedTests) {
            String testCaseId = String.valueOf(item.getTestCaseId());
            String member = ContextHolder.getValue(testCaseId);
            failedCount.merge(member, 1, Integer::sum);
        }

        //проход по успешным тестам
        for (TestResult.ContentItem item : passedTests) {
            String testCaseId = String.valueOf(item.getTestCaseId());
            String member = ContextHolder.getValue(testCaseId);
            passedCount.merge(member, 1, Integer::sum);
        }

        allCount.forEach((author, testsCount) -> {
            int failed = failedCount.get(author) == null ? 0 : failedCount.get(author);
            int passed = passedCount.get(author) == null ? 0 : passedCount.get(author);
            String percent = new DecimalFormat("##.##").format(passed / (allCount.get(author) / 100.0));
            builder.append(author).append(": ").append(testsCount)
                    .append(" => ").append(passed).append(" - ").append(percent).append("%").append("\n");
        });
        return builder.toString();
    }
}
