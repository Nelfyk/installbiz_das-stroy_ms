package ru.installbiz.das_stroy;

import ru.installbiz.das_stroy.entity.Config;
import ru.installbiz.das_stroy.entity.Response;
import ru.installbiz.das_stroy.service.ApiService;
import ru.installbiz.das_stroy.service.MessageHandlerService;
import ru.installbiz.das_stroy.service.StorageService;
import ru.installbiz.das_stroy.service.XLSHandlerService;

import java.util.function.Function;

public class Handler implements Function<String, Response> {

    @Override
    public Response apply(String s) {
        try {
            ApiService apiService = new ApiService();
            apiService.build(MessageHandlerService.getId(s));
            new XLSHandlerService().start();
            apiService.insertLink(StorageService.putFile(Config.getFullFilePath()));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new Response(200, "Done");
    }
}
