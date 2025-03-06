package com.beauty1nside.stdr.controller;

import com.beauty1nside.common.FileConfig;
import com.beauty1nside.common.GridArray;
import com.beauty1nside.common.Paging;
import com.beauty1nside.stdr.dto.DocumentDTO;
import com.beauty1nside.security.service.CustomerUser;
import com.beauty1nside.stdr.dto.DocumentSearchDTO;
import com.beauty1nside.stdr.service.StdrDeptService;
import com.beauty1nside.stdr.service.DocumentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Log4j2
@RestController
@AllArgsConstructor
@RequestMapping("/api/stdr/*")
public class StdrRestController {
  final DocumentService documentService;
  final StdrDeptService stdrDeptService;
  final FileConfig fileConfig;
  
  @GetMapping("document")
  public Object getDocList(@RequestParam(name = "perPage", defaultValue = "20", required = false) int perPage,
                           @RequestParam(name = "page", defaultValue = "1", required = false) int page,
                           DocumentSearchDTO dto,
                           Paging paging,
                           @AuthenticationPrincipal CustomerUser user) throws JsonProcessingException  {
    
    paging.setPageUnit(perPage);
    paging.setPage(page);
    
    dto.setStart(paging.getFirst());
    dto.setEnd(paging.getLast());
    
    Long companyNum = user.getUserDTO().getCompanyNum();
    dto.setCompanyNum(companyNum);
    paging.setTotalRecord(documentService.count(dto));
    
    GridArray grid = new GridArray();
    
    return grid.getArray(paging.getPage(), documentService.count(dto), documentService.documentList(dto));
  }
  
  @GetMapping("/document/{id}")
  public DocumentDTO getApprovalContent(@PathVariable("id") Long documentId,
                                        DocumentDTO dto,
                                        @AuthenticationPrincipal CustomerUser user) {
    Long companyNum = user.getUserDTO().getCompanyNum();
    dto.setCompanyNum(companyNum);
    dto.setDocumentId(documentId);
    return documentService.info(dto);
  }
  
  @PostMapping("document")
  public Object insertDocument(@RequestBody DocumentDTO dto, @AuthenticationPrincipal CustomerUser user) {
    Long companyNum = user.getUserDTO().getCompanyNum();
    dto.setCompanyNum(companyNum);
    
    return documentService.insert(dto);
  }
  
  @PostMapping("document/img")
  public ResponseEntity<Map<String, String>> uploadImage(@RequestParam("image") MultipartFile file) {
    Map<String, String> response = new HashMap<>();
    try {
      // 파일명 생성 (UUID + 원본 확장자)
      String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
      String uploadDir = fileConfig.getUpload();
      Path uploadPath = Paths.get(uploadDir + fileName);
      
      // 저장 디렉토리 없으면 생성
      Files.createDirectories(uploadPath.getParent());
      Files.write(uploadPath, file.getBytes());
      
      // 클라이언트에 제공할 이미지 URL 생성
      String imageUrl = uploadDir + fileName;
      response.put("imageUrl", imageUrl);
      
      return ResponseEntity.ok(response);
    } catch (IOException e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }
  
  
  @GetMapping("dept")
  public Object getHqDeptList(@AuthenticationPrincipal CustomerUser user) {
    Long companyNum = user.getUserDTO().getCompanyNum();
    return stdrDeptService.hqDeptList(companyNum);
  }
}
