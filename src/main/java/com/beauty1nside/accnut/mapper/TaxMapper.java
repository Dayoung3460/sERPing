package com.beauty1nside.accnut.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface TaxMapper {
	int insertHeader(@Param("from") Map<String, Object> from, @Param("to") Map<String, Object> to, @Param("total") Map<String, Object> total, @Param("pk") int pk);
	int insertDetail(@Param("pk") int pk, @Param("detail") Map<String, Object> detail);
	int maxHeader();
	Map<String, Object> info(int pk);
	
}
