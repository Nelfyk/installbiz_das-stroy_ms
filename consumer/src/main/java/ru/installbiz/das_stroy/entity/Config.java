package ru.installbiz.das_stroy.entity;


public class Config {
    private static final String API_LOGIN = System.getenv("MS_LOGIN");
    private static final String API_PASSWORD = System.getenv("MS_PASSWORD");

    private static final String AWS_ID = System.getenv("AWS_ACCESS_KEY_ID");
    private static final String AWS_KEY = System.getenv("AWS_SECRET_ACCESS_KEY");
    private static final String BUCKET_NAME = System.getenv("BUCKET_NAME");

    public static final String PATH = "/tmp/";
    public static final String TEMPLATE_NAME = System.getenv("TEMPLATE_NAME");
    public static final String FORMAT = ".xls";
    public static final String LINK_NAME = System.getenv("LINK_NAME");
    // ID счета
    public static String id;
    // Номер счета
    private static String name;
    // Кол-во позиций
    private static int positionSize;

    public static int getPositionSize() {
        return positionSize;
    }

    public static void setPositionSize(int positionSize) {
        Config.positionSize = positionSize;
    }

    public static String getId() {
        return id;
    }

    public static void setId(String id) {
        Config.id = id;
    }

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        Config.name = name;
    }

    public static String getBUCKET_NAME() {
        return BUCKET_NAME;
    }

    public static String getAWS_ID() {
        return AWS_ID;
    }

    public static String getAWS_KEY() {
        return AWS_KEY;
    }

    public static String getAPI_LOGIN() {
        return API_LOGIN;
    }

    public static String getAPI_PASSWORD() {
        return API_PASSWORD;
    }

    public static String getFullFilePath() {
        return PATH + TEMPLATE_NAME + "-" + name + FORMAT;
    }

    public static String getFullTemplatePath() {
        return PATH + TEMPLATE_NAME + FORMAT;
    }
}