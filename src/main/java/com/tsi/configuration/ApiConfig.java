package com.tsi.configuration;

import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ApiConfig {

    public List<String> apiList = new ArrayList<String>();

    public ApiConfig() {
        apiList.add("http://localhost:8091/repo1/rest");
        apiList.add("http://localhost:8092/repo2/rest");
    }

    public List<String> getApiList() {
        return apiList;
    }

    public String getCorrectApi(String location) {
        for (String api: apiList) {
            if (location.contains(api)) {
                System.out.println("Repository API location: " + api);
                return api;
            }
        }
        throw new IllegalArgumentException("Repository API is not registered " + location);
    }
}
