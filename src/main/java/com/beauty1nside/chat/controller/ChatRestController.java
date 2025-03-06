package com.beauty1nside.chat.controller;

import com.beauty1nside.chat.dto.MessageDTO;
import com.beauty1nside.chat.dto.RoomDTO;
import com.beauty1nside.chat.service.ChatService;
import com.beauty1nside.common.FileConfig;
import com.beauty1nside.hr.dto.EmpDTO;
import com.beauty1nside.security.service.CustomerUser;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.nio.file.Path;
import java.nio.file.Files;
@Log4j2
@RestController
@AllArgsConstructor
@RequestMapping("/api/chat/*")
@CrossOrigin(origins = "*") // 모든 도메인 허용
public class ChatRestController {
  final ChatService chatService;
  final FileConfig fileConfig;
  
  @GetMapping("/employees")
  public List<EmpDTO> getEmpList(@AuthenticationPrincipal CustomerUser user) {
    Long companyNum = user.getUserDTO().getCompanyNum();
    return chatService.empList(companyNum);
  }
  
  @GetMapping("/start/{employeeNum}")
  public Map<Long, List<MessageDTO>> startChat(@PathVariable(name="employeeNum") Long employeeNum,
                                               @AuthenticationPrincipal CustomerUser user) {
    RoomDTO roomDTO = new RoomDTO();
    roomDTO.setCompanyNum(user.getUserDTO().getCompanyNum());
    roomDTO.setEmployeeNum1(user.getUserDTO().getEmployeeNum());
    roomDTO.setEmployeeNum2(employeeNum);
    
    return chatService.startChat(roomDTO);
  }
  
  @PostMapping("/msg")
  public ResponseEntity<Map<String, Object>> sendMsg(@RequestBody MessageDTO messageDTO,
                                                     @AuthenticationPrincipal CustomerUser user) {
    messageDTO.setEmployeeNum(user.getUserDTO().getEmployeeNum());
    
    MessageDTO dto = chatService.sendMsg(messageDTO);
    Map<String, Object> response = new HashMap<>();
    response.put("data", dto);
    return ResponseEntity.ok(response);
  }
  
  @PostMapping("/img")
  public ResponseEntity<Map<String, Object>> uploadFile(@RequestPart(value = "image", required = false) MultipartFile file,
                                                        @RequestBody MessageDTO messageDTO,
                                                     @AuthenticationPrincipal CustomerUser user) {

    log.info("imgimgimg");
    Map<String, Object> response = new HashMap<>();
    try {
      // 저장할 경로 설정
      String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
      String uploadDir = fileConfig.getUploadpath();
      Path filePath = Paths.get(uploadDir + fileName);
      
      // 파일 저장
      Files.createDirectories(filePath.getParent());
      Files.write(filePath, file.getBytes());
      
      String imageUrl = fileConfig.getImgpath() + fileName;
      
      messageDTO.setEmployeeNum(user.getUserDTO().getEmployeeNum());
      messageDTO.setImgPath(imageUrl);
      MessageDTO dto = chatService.sendImg(messageDTO);
      
      response.put("success", true);
      response.put("data", dto);
      
      return ResponseEntity.ok(response);
    } catch (IOException e) {
      e.printStackTrace();
      response.put("success", false);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
  }
}
