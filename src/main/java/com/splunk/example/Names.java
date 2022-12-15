package com.splunk.example;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Names {

    private final static List<String> names = Arrays.asList(
            "Liam","Olivia",
            "Noah","Emma",
            "Oliver","Charlotte",
            "Elijah","Amelia",
            "James","Ava",
            "William","Sophia",
            "Benjamin","Isabella",
            "Lucas","Mia",
            "Henry","Evelyn",
            "Theodore","Harper");

    private final static Random rand = new Random();

    public static String random() {
        return names.get(rand.nextInt(names.size()));
    }
}
