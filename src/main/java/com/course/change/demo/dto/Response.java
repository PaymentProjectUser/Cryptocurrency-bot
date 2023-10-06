package com.course.change.demo.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Response {
	private List<CryptocurrencyDto> response;
}