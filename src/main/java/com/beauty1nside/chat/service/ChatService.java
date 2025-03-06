package com.beauty1nside.chat.service;

import com.beauty1nside.chat.dto.MessageDTO;
import com.beauty1nside.chat.dto.RoomDTO;
import com.beauty1nside.hr.dto.EmpDTO;

import java.util.List;
import java.util.Map;

public interface ChatService {
  List<EmpDTO> empList(Long companyNum);
  Map<Long, List<MessageDTO>> startChat(RoomDTO roomDTO);
  MessageDTO sendMsg(MessageDTO messageDTO);
  MessageDTO sendImg(MessageDTO messageDTO);
}
