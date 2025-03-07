package com.beauty1nside.purchs.dto;

import lombok.Data;

@Data
public class OrderlistSearchDTO extends OrderlistDTO{
	int start;
	int end;
	int pageUnit;
}
