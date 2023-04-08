package ru.installbiz.das_stroy;

import ru.installbiz.das_stroy.service.ApiService;

public class Test {
    public static void main(String[] args) {
        try {
            new ApiService().poll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
