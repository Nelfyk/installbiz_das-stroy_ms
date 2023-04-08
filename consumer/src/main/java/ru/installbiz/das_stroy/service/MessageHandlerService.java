package ru.installbiz.das_stroy.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.installbiz.das_stroy.entity.Config;
import ru.installbiz.das_stroy.entity.QueueMessage;

// Переводит входящий JSON в QueueMessage.class и возвращает тело сообщения
public class MessageHandlerService {
    public static String getId(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            QueueMessage message = objectMapper.readValue(json, QueueMessage.class);
            Config.setId(message.getMessages().get(0).getDetails().getMessage().getBody());
            return message.getMessages().get(0).getDetails().getMessage().getBody();
        } catch (JsonProcessingException e) {
            return "error";
        }
    }
}
