package com.course.change.demo.mapper;

import com.course.change.demo.dto.CryptocurrencyDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class CourseChangeMapper {
    public List<CryptocurrencyDto> convertToList(Map<String, Double> newCurrencies) {
        List<CryptocurrencyDto> result = new ArrayList<>();
        for (String currentSymbol : newCurrencies.keySet()) {
            result.add(CryptocurrencyDto.builder()
                    .symbol(currentSymbol)
                    .price(newCurrencies.get(currentSymbol))
                    .build());
        }
        return result;
    }
}
