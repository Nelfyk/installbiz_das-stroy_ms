package ru.installbiz.das_stroy;

import ru.installbiz.das_stroy.entity.Response;
import ru.installbiz.das_stroy.service.ApiService;

import java.util.function.Function;

public class Handler implements Function<String, Response> {
    @Override
    public Response apply(String s) {
        try {
            new ApiService().poll();
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
        return new Response(200, "Done");
    }
}
