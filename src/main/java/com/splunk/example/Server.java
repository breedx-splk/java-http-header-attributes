package com.splunk.example;

import static spark.Spark.port;
import static spark.Spark.post;

public class Server {
    public void runForever() {
        port(8182);
        post("/greeting", (req, res) -> {
            String demeanorHeader = req.headers("demeanor");
            String emoji = Demeanor.valueOf(demeanorHeader).emoji;
            res.type("text/plain");
            res.header("originator", req.body().toUpperCase());
            return req.body() + " => " + emoji;
        });
    }
}
