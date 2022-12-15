package com.splunk.example;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Client {
    private final ScheduledExecutorService pool = Executors.newSingleThreadScheduledExecutor();
    private final HttpClient client = HttpClient.newBuilder().build();

    public void runForever() {
        pool.scheduleAtFixedRate(this::doRequest, 1, 1, TimeUnit.SECONDS);
    }

    private void doRequest() {
        String name = Names.random();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8182/greeting"))
                .header("demeanor", Demeanor.random().toString())
                .header("Content-Type", "text/plain")
                .POST(HttpRequest.BodyPublishers.ofString(name))
                .build();
        try {
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
            System.out.println("client received: " + response.body());
        } catch (Exception e) {
            System.out.println("Client error:");
            e.printStackTrace();
        }
    }
}
