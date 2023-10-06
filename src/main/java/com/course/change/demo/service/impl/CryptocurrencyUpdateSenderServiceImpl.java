package com.course.change.demo.service.impl;

import com.course.change.demo.dto.CourseChangeResponseDto;
import com.course.change.demo.dto.CryptocurrencyDto;
import com.course.change.demo.dto.Response;
import com.course.change.demo.mapper.CourseChangeMapper;
import com.course.change.demo.mapper.CryptocurrencyMapper;
import com.course.change.demo.model.Cryptocurrency;
import com.course.change.demo.repository.CryptocurrencyRepository;
import com.course.change.demo.service.CourseChangeBotService;
import com.course.change.demo.service.CourseChangeRestClient;
import com.course.change.demo.service.CryptocurrencyUpdateSenderService;
import com.course.change.demo.service.UserService;
import lombok.AllArgsConstructor;
import org.jvnet.hk2.annotations.Service;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CryptocurrencyUpdateSenderServiceImpl implements CryptocurrencyUpdateSenderService {
    private final static Integer MAX_PERCENT = 100;
    private final static Integer USER_PERCENT = 15;

    private final CryptocurrencyRepository cryptocurrencyRepository;
    private final CourseChangeRestClient courseChangeRestClient;
    private final CryptocurrencyMapper cryptocurrencyMapper;
    private final CourseChangeBotService courseChangeBotService;
    private final UserService userService;
    private final CourseChangeMapper courseChangeMapper;

    @Scheduled(cron = "${scheduling.cron.cryptocurrency-update-time:-}")
    public void update() {
        List<Cryptocurrency> oldCryptocurrencyList = cryptocurrencyRepository.findAll();
        Response gather = courseChangeRestClient.gather();

        if (!(gather == null || gather.getResponse() == null || gather.getResponse().isEmpty())) {
            List<CryptocurrencyDto> cryptocurrencyDtos = cryptocurrencyMapper.mapToDto(oldCryptocurrencyList);
            List<CryptocurrencyDto> updatedCurrencies = gather.getResponse().stream()
                    .filter(item -> !cryptocurrencyDtos.contains(item))
                    .toList();

            CourseChangeResponseDto courseChangeResponseDto =
                    getDifferentCurrencyDate(cryptocurrencyDtos, updatedCurrencies);

            courseChangeBotService.sendMessages(userService.getAllChatIds(), courseChangeResponseDto);

            updateCryptocurrenciesInfo(updatedCurrencies,
                    courseChangeMapper.convertToList(courseChangeResponseDto.getNewCurrencies()));
        }
    }
    
    public CourseChangeResponseDto getDifferentCurrencyDate(List<CryptocurrencyDto> oldCryptocurrencyDtos,
                                                 List<CryptocurrencyDto> cryptocurrencyDtos) {
        Map<String, Double> updatedCurremciesValueMap = new HashMap<>();
        Map<String, Double> newCurremciesValueMap = new HashMap<>();
        Map<String, Double> oldCryptocurrencyMap = oldCryptocurrencyDtos.stream()
                .collect(Collectors.toMap(CryptocurrencyDto::getSymbol,
                        CryptocurrencyDto::getPrice));

        for (CryptocurrencyDto cryptocurrencyDto : cryptocurrencyDtos) {
            Double oldValue = oldCryptocurrencyMap.get(cryptocurrencyDto.getSymbol());
            if (oldValue != null && isDifferenceInPercentGreaterThanMax(oldValue, cryptocurrencyDto.getPrice())) {
                updatedCurremciesValueMap.put(cryptocurrencyDto.getSymbol(), cryptocurrencyDto.getPrice() -
                        oldValue);
            } else {
                newCurremciesValueMap.put(cryptocurrencyDto.getSymbol(), cryptocurrencyDto.getPrice());
            }
        }

        return CourseChangeResponseDto.builder()
                .changedCurrencies(updatedCurremciesValueMap)
                .newCurrencies(newCurremciesValueMap)
                .build();
    }

    private boolean isDifferenceInPercentGreaterThanMax(Double oldValue, Double newValue) {
        return ((newValue - oldValue) / oldValue) * MAX_PERCENT > USER_PERCENT;
    }

    private void updateCryptocurrenciesInfo(List<CryptocurrencyDto> updatedCurrencies,
                                            List<CryptocurrencyDto> newCurrencies) {

        Map<String, Double> updatedCryptocurrencyMap = updatedCurrencies.stream()
                .collect(Collectors.toMap(CryptocurrencyDto::getSymbol,
                        CryptocurrencyDto::getPrice));

        List<Cryptocurrency> cryptocurrencyList = cryptocurrencyRepository.findAllIdsByTestPlayerIs(updatedCurrencies.stream()
                .map(CryptocurrencyDto::getSymbol)
                .toList());

        for (Cryptocurrency cryptocurrency : cryptocurrencyList) {
            Double value = updatedCryptocurrencyMap.get(cryptocurrency.getSymbolName());
            cryptocurrency.setCurrentPrice(value);
        }
        cryptocurrencyRepository.saveAll(cryptocurrencyList);
        cryptocurrencyRepository.saveAll(cryptocurrencyMapper.mapToModel(newCurrencies));
    }
}
