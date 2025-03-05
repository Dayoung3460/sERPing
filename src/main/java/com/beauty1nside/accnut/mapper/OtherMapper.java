package com.beauty1nside.accnut.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface OtherMapper {
	List<Map<String, Object>> optionList(@Param("goodsName") String goodsName, @Param("companyNum") int companyNum);
	List<Map<String, Object>> bhfList(@Param("companyNum") int companyNum);
	Map<String, Object> companyInfo(@Param("companyNum") int companyNum);
	List<Map<String, Object>> deptList(@Param("companyNum") int companyNum);
	List<Map<String, Object>> dayList(@Param("companyNum") int companyNum, @Param("bhfId") String bhfId, @Param("month") String month);
	List<Map<String, Object>> closingList(@Param("code") String code);
	
}
