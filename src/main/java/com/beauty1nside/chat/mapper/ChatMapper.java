package com.beauty1nside.chat.mapper;

import com.beauty1nside.chat.dto.MessageDTO;
import com.beauty1nside.chat.dto.RoomDTO;
import com.beauty1nside.hr.dto.EmpDTO;

import java.util.List;

public interface ChatMapper {
  List<EmpDTO> empList(Long companyNum);
  RoomDTO getChatRoom(RoomDTO roomDTO);
  void insertChatRoom(RoomDTO roomDTO);
  RoomDTO getRoomJustMade();
  List<MessageDTO> getMsgList(Long roomId);
  void insertMsg(MessageDTO messageDTO);
  void insertImgPath(MessageDTO messageDTO);
  MessageDTO getMsgJustSent(Long roomId);
  
}
