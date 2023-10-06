package com.course.change.demo.service;

import com.course.change.demo.dto.CourseChangeResponseDto;

import java.util.List;

public interface CourseChangeBotService {
    void sendMessages(List<Long> chatIds, CourseChangeResponseDto courseChangeResponseDto);
}
