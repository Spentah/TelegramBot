package com.example.demo.api;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service
public class RequestSender {

    private static final Logger LOGGER = Logger.getLogger(RequestSender.class);
    private static final String BASE_URL = "https://allure-gorod.proitr.ru/api/rs";
    private static final String TOKEN = "b6b9bbff-275b-4d90-83cb-c67a8e51276d";

    @Autowired
    private RestTemplate restTemplate;

    public HttpEntity setTokenHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.ALL));
        headers.set("Authorization", "Api-Token " + TOKEN);
        return new HttpEntity(headers);
    }

    public ResponseEntity<String> sendRequest(String endpoint, HttpMethod method, Object... params) {
        ResponseEntity<String> response = restTemplate.exchange(
                BASE_URL + endpoint, method, setTokenHeader(), String.class, params
        );
        if (response.getStatusCodeValue() != 200) {
            LOGGER.error("Запрос не прошел! Эндпоинт - '" + endpoint + "', статус код - '" + response.getStatusCodeValue());
            return null;
        } else {
            LOGGER.info("Отправлен запрос '" + endpoint + "'");
            return response;
        }
    }

    public String getLaunchStatistic(String id) {
        String endPoint = String.format("/launch/%s/statistic", id);
        return sendRequest(endPoint, HttpMethod.GET).getBody();
    }

    public String getEnvironment(String id) {
        String endpoint = String.format("/launch/%s/env", id);
        return sendRequest(endpoint, HttpMethod.GET).getBody();
    }


    public String getTestResult(String id) {
        String endpoint = "/testresult?launchId={id}&page=0&size={size}&sort=";
        return sendRequest(endpoint, HttpMethod.GET, id, 1000).getBody();
    }

    public String getTestCaseMember(String testCaseId) {
        String endpoint = String.format("/testcase/{id}/members");
        return sendRequest(endpoint, HttpMethod.GET, testCaseId).getBody();
    }
}
