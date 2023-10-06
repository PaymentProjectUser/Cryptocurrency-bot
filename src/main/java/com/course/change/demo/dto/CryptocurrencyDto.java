package com.course.change.demo.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode
public class CryptocurrencyDto {
	private String symbol;
	private Double price;
}