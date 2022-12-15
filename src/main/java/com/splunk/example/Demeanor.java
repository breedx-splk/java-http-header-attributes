package com.splunk.example;

import java.util.Arrays;
import java.util.Random;

public enum Demeanor {
    HAPPY("\uD83D\uDE0A"),
    SAD("\uD83D\uDE22"),
    TIRED("\uD83E\uDD71"),
    CURIOUS("\uD83E\uDD14"),
    ANGRY("\uD83D\uDE21"),
    LOL("\uD83E\uDD23");

    static final Random random = new Random();
    public final String emoji;

    Demeanor(String emoji) {
        this.emoji = emoji;
    }

    static Demeanor random(){
        int i = random.nextInt(Demeanor.values().length);
        return Demeanor.values()[i];
    }
}
