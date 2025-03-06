package com.beauty1nside.accnut.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface OtherService {
	List<Map<String, Object>> optionList(String goodsName, int companyNum);
	List<Map<String, Object>> bhfList(int companyNum);
	Map<String, Object> companyInfo(int companyNum);
	List<Map<String, Object>> deptList(@Param("companyNum") int companyNum);
	List<Map<String, Object>> dayList(@Param("companyNum") int companyNum, @Param("bhfId") String bhfId, @Param("month") String month);
	List<Map<String, Object>> closingList(@Param("code") String code);
}
