package ru.installbiz.das_stroy;

import ru.installbiz.das_stroy.entity.Config;
import ru.installbiz.das_stroy.service.ApiService;
import ru.installbiz.das_stroy.service.MessageHandlerService;
import ru.installbiz.das_stroy.service.StorageService;
import ru.installbiz.das_stroy.service.XLSHandlerService;

public class Test {
    public static void main(String[] args) {
        try {
            long startTime = System.currentTimeMillis();
            ApiService apiService = new ApiService();
            apiService.build(MessageHandlerService.getId("{\n" +
                    "   \"messages\":[\n" +
                    "      {\n" +
                    "         \"event_metadata\":{\n" +
                    "            \"event_id\":\"a63a586b-f8f0f761-32ae2be7-7fbbce87\",\n" +
                    "            \"event_type\":\"yandex.cloud.events.messagequeue.QueueMessage\",\n" +
                    "            \"created_at\":\"2023-03-25T08:23:51.995Z\",\n" +
                    "            \"tracing_context\":null,\n" +
                    "            \"cloud_id\":\"b1g44ark04qfrt59tot6\",\n" +
                    "            \"folder_id\":\"b1g3cnh3lmskpufdk3gk\"\n" +
                    "         },\n" +
                    "         \"details\":{\n" +
                    "            \"queue_id\":\"yrn:yc:ymq:ru-central1:b1g3cnh3lmskpufdk3gk:test\",\n" +
                    "            \"message\":{\n" +
                    "               \"message_id\":\"a63a586b-f8f0f761-32ae2be7-7fbbce87\",\n" +
                    "               \"md5_of_body\":\"aefab75a2c545f90e4e7eabbade20bbe\",\n" +
                    "               \"body\":\"2fda5f44-ce3e-11ed-0a80-062900291043\",\n" +
                    "               \"attributes\":{\n" +
                    "                  \"ApproximateFirstReceiveTimestamp\":\"1679732632282\",\n" +
                    "                  \"ApproximateReceiveCount\":\"1\",\n" +
                    "                  \"SentTimestamp\":\"1679732631995\"\n" +
                    "               },\n" +
                    "               \"message_attributes\":{\n" +
                    "                  \n" +
                    "               },\n" +
                    "               \"md5_of_message_attributes\":\"\"\n" +
                    "            }\n" +
                    "         }\n" +
                    "      }\n" +
                    "   ]\n" +
                    "}"));
            new XLSHandlerService().start();
            apiService.insertLink(StorageService.putFile(Config.getFullFilePath()));
            apiService.deleteTempFiles();
            System.out.println(((double) (System.currentTimeMillis() - startTime)) / 1000);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
