package com.course.change.demo.service.impl;

import com.course.change.demo.dto.Response;
import com.course.change.demo.service.CourseChangeRestClient;
import org.jvnet.hk2.annotations.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class CourseChangeRestClientServiceImpl implements CourseChangeRestClient {
    public static final String STATUS_OK = "ok";
    private final static String URL = "https://api.mexc.com/api/v3/ticker/price";

    @Override
    public Response gather() {
        return WebClient.create()
                .get()
                .uri(URL)
                .retrieve()
                .bodyToMono(Response.class)
                .block();
    }
}
