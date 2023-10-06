package com.course.change.demo.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
@Getter
@Builder
public class CourseChangeResponseDto {
    private final Map<String, Double> changedCurrencies;
    private final Map<String, Double> newCurrencies;
}
