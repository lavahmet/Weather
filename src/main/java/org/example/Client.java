package org.example;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;


public class Client {
    public static void main(String[] args) {
        final String sensorName = "SensorName1";

        registerSesnsor(sensorName);

        Random random = new Random();

        double maxTemperature = 45.0;
        for (int i = 0; i < 50 ; i++) {
            System.out.println(i);
            sendMeasurement(random.nextDouble() * maxTemperature, random.nextBoolean(), sensorName);

        }
    }

    private static void registerSesnsor(String sensorName) {
        final String url = "http://localhost:8080/sensors/registration";

        LinkedHashMap<String, Object> jsonData = new LinkedHashMap<>();
        jsonData.put("name", sensorName);

        makePostRequestWithJSONData(url, jsonData);
    }

    private static void sendMeasurement(double value, boolean raining, String sensorName) {
        final String url = "http://localhost:8080/meaurements/add";

        LinkedHashMap<String, Object> sens = new LinkedHashMap<>();
        sens.put("name", sensorName);
        LinkedHashMap<String, Object> jsonData = new LinkedHashMap<>();
        jsonData.put("value", value);
        jsonData.put("raining", raining);
        jsonData.put("sensor", sens);

        makePostRequestWithJSONData(url, jsonData);
    }

    private static void makePostRequestWithJSONData(String url, LinkedHashMap<String, Object> jsonData) {
        final RestTemplate restTemplate = new RestTemplate();

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Object> request = new HttpEntity<>(jsonData, headers);

        try {
            restTemplate.postForObject(url, request, String.class);

            System.out.println("Measurement successfully sent to the server");
        } catch (HttpClientErrorException e) {
            System.out.println("Error");
            System.out.println(e.getMessage());
        }

    }
}
