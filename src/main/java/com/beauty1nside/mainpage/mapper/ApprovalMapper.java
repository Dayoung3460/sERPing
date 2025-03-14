package com.beauty1nside.mainpage.mapper;

import com.beauty1nside.mainpage.dto.ApprovalDTO;
import com.beauty1nside.mainpage.dto.ApprovalSearchDTO;
import com.beauty1nside.stdr.dto.DocumentDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ApprovalMapper {
	ApprovalDTO info(@Param("inApprovalId") Long inApprovalId, @Param("companyNum") Long companyNum);
	List<ApprovalDTO> waitingList(@Param("dto") ApprovalSearchDTO dto, @Param("companyNum") Long companyNum);
	List<ApprovalDTO> myList(ApprovalSearchDTO dto);
	int count(@Param("dto") ApprovalSearchDTO dto, @Param("companyNum") Long companyNum);
	int myCount(ApprovalSearchDTO dto);
	int update(ApprovalDTO dto);
	int insert(ApprovalDTO dto);
	List<DocumentDTO> documentList(Long companyNum);
}
