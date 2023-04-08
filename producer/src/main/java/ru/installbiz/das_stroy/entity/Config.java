package ru.installbiz.das_stroy.entity;

public class Config {
    private static final String API_LOGIN = System.getenv("MS_LOGIN");
    private static final String API_PASSWORD = System.getenv("MS_PASSWORD");
    private static final String QUEUE_URL = System.getenv("QUEUE_URL");
    public static final String ATTRIBUTE_NAME = System.getenv("ATTRIBUTE_NAME");

    public static String getQUEUE_URL() {
        return QUEUE_URL;
    }

    public static String getAPI_LOGIN() {
        return API_LOGIN;
    }

    public static String getAPI_PASSWORD() {
        return API_PASSWORD;
    }
}

