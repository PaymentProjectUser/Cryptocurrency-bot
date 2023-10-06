package com.course.change.demo.mapper;

import com.course.change.demo.dto.CryptocurrencyDto;
import com.course.change.demo.model.Cryptocurrency;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class CryptocurrencyMapper {
    public List<CryptocurrencyDto> mapToDto(List<Cryptocurrency> cryptocurrencies) {
        List<CryptocurrencyDto> result = new ArrayList<>();
        for (Cryptocurrency cryptocurrency : cryptocurrencies) {
            result.add(CryptocurrencyDto.builder()
                    .price(cryptocurrency.getCurrentPrice())
                    .symbol(cryptocurrency.getSymbolName())
                    .build());
        }
        return result;
    }

    public List<Cryptocurrency> mapToModel(List<CryptocurrencyDto> cryptocurrencies) {
        List<Cryptocurrency> result = new ArrayList<>();
        for (CryptocurrencyDto cryptocurrency : cryptocurrencies) {
            result.add(Cryptocurrency.builder()
                    .currentPrice(cryptocurrency.getPrice())
                    .symbolName(cryptocurrency.getSymbol())
                    .build());
        }
        return result;
    }
}
