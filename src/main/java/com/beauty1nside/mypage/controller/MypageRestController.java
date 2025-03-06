package com.beauty1nside.mypage.controller;

import com.beauty1nside.common.FileConfig;
import com.beauty1nside.hr.dto.EmpDTO;
import com.beauty1nside.mainpage.dto.ApprovalDTO;
import com.beauty1nside.mainpage.service.ApprovalService;
import com.beauty1nside.mypage.service.ProfileService;
import com.beauty1nside.security.service.CustomerUser;
import com.beauty1nside.security.service.UserDTO;
import com.beauty1nside.stdr.dto.DocumentDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
@RequestMapping("/api/mypage/*")
public class MypageRestController {
  final ApprovalService approvalService;
  final ProfileService profileService;
  final FileConfig fileConfig;
  
  @GetMapping("/profile")
  public EmpDTO getMyProfile(@AuthenticationPrincipal CustomerUser user) {
    EmpDTO empDTO = new EmpDTO();
    
    empDTO.setEmployeeNum(user.getUserDTO().getEmployeeNum());
    empDTO.setCompanyNum(user.getUserDTO().getCompanyNum());
    return profileService.info(empDTO);
  }
  
  @PostMapping("/profile/img")
  public ResponseEntity<Map<String, Object>> updateProfileImg(@RequestParam("image") MultipartFile file,
                                                              @AuthenticationPrincipal CustomerUser user,
                                                              HttpServletRequest request) {
    EmpDTO empDTO = new EmpDTO();
    empDTO.setEmployeeNum(user.getUserDTO().getEmployeeNum());
    empDTO.setCompanyNum(user.getUserDTO().getCompanyNum());
    
    Map<String, Object> response = new HashMap<>();
    
    try {
      String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

      String uploadDir = fileConfig.getUpload();
      Path uploadPath = Paths.get(uploadDir + fileName);
      
      Files.createDirectories(uploadPath.getParent());
      Files.write(uploadPath, file.getBytes());
      
      String imageUrl = uploadDir + fileName;
      empDTO.setProfileImage(imageUrl);
      
      int isSuccess = profileService.update(empDTO);
      if (isSuccess == 1) {
        UserDTO userDTO = new UserDTO();
        userDTO.setProfileImage(imageUrl);
        request.getSession().setAttribute("profileImage", userDTO.getProfileImage());
        
        response.put("message", "success");
        return ResponseEntity.ok(response);
      } else {
        response.put("message", "fail");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
      }
    } catch (IOException e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }
  
  @PostMapping("/approval")
  public ResponseEntity<Map<String, Object>> submitApproval(@RequestBody DocumentDTO documentDTO,
                                                            @AuthenticationPrincipal CustomerUser user) {
    Map<String, Object> response = new HashMap<>();
    ApprovalDTO approvalDTO = new ApprovalDTO();
    
    Long companyNum = user.getUserDTO().getCompanyNum();
    approvalDTO.setCompanyNum(companyNum);
    
    Long employeeNum = user.getUserDTO().getEmployeeNum();
    approvalDTO.setEmployeeNum(employeeNum);
    
    approvalDTO.setDocumentId(documentDTO.getDocumentId());
    approvalDTO.setInApprovalRequestContent(documentDTO.getDocumentTemplate());
    
    
    try {
      int isSuccess = approvalService.insert(approvalDTO);
      if (isSuccess == 1) {
        response.put("message", "success");
        return ResponseEntity.ok(response);
      } else {
        response.put("message", "fail");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
      }
    } catch (Exception e) {
      
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
    
  }

}
