package com.beauty1nside.accnut.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.beauty1nside.accnut.dto.AssetDTO;
import com.beauty1nside.accnut.dto.AssetSearchDTO;
import com.beauty1nside.accnut.dto.SellingSearchDTO;
import com.beauty1nside.accnut.service.AssetService;
import com.beauty1nside.accnut.service.ChartingService;
import com.beauty1nside.accnut.service.DealBookService;
import com.beauty1nside.accnut.service.DebtService;
import com.beauty1nside.accnut.service.EtcBookService;
import com.beauty1nside.accnut.service.IncidentalCostService;
import com.beauty1nside.accnut.service.JsonQueryService;
import com.beauty1nside.accnut.service.OtherService;
import com.beauty1nside.accnut.service.SalaryBookService;
import com.beauty1nside.accnut.service.SellingService;
import com.beauty1nside.accnut.service.TaxService;
import com.beauty1nside.common.GridArray;
import com.beauty1nside.common.Paging;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2	//log4j 가 안되면 버전높은 log4j2 사용
@RestController
@AllArgsConstructor
@RequestMapping("/accnut/rest/*")
public class AccnutChartController {
	private final ChartingService sellingService;
	
	final SellingService tempService;
	
	@GetMapping("/selling/chartinfo")
	public Object chartinfo(@RequestParam(name = "perPage", defaultValue = "99999", required = false) int perPage, 
			@RequestParam(name = "page", defaultValue = "1", required = false) int page, 
			SellingSearchDTO dto, Paging paging) throws JsonMappingException, JsonProcessingException {
		// 페이징 유닛 수
		paging.setPageUnit(perPage);
		paging.setPage(page);
		
		log.info(dto);
		
		// 페이징 조건
		dto.setStart(paging.getFirst());
		dto.setEnd(paging.getLast());
		
		// 페이징 처리
		paging.setTotalRecord(tempService.listCount(dto));
		
		// grid 배열 처리
		GridArray grid = new GridArray();
		Object result = grid.getArray( paging.getPage(), tempService.listCount(dto), sellingService.chartinfo(dto) );
		return result;
	}
}
