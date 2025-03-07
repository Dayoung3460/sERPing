package com.beauty1nside.chat.service.impl;

import com.beauty1nside.chat.dto.MessageDTO;
import com.beauty1nside.chat.dto.RoomDTO;
import com.beauty1nside.chat.mapper.ChatMapper;
import com.beauty1nside.chat.service.ChatService;
import com.beauty1nside.hr.dto.EmpDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
  private final ChatMapper chatMapper;
  
  @Override
  public List<EmpDTO> empList(Long companyNum) {
    return chatMapper.empList(companyNum);
  }
  
  @Override
  @Transactional
  public Map<Long, List<MessageDTO>> startChat(RoomDTO roomDTO) {
    RoomDTO dto = chatMapper.getChatRoom(roomDTO);
    Map<Long, List<MessageDTO>> returnValue = new HashMap<>();
    
    if (dto == null) {
      try {
        chatMapper.insertChatRoom(roomDTO);
        RoomDTO newRoomDTO = chatMapper.getRoomJustMade();
        return (Map<Long, List<MessageDTO>>) returnValue.put(newRoomDTO.getRoomId(), List.of());
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    } else {
      returnValue.put(dto.getRoomId(), chatMapper.getMsgList(dto.getRoomId()));
      return returnValue;
    }
  }
  
  @Override
  @Transactional
  public MessageDTO sendMsg(MessageDTO messageDTO) {
    chatMapper.insertMsg(messageDTO);
    return chatMapper.getMsgJustSent(messageDTO.getRoomId());
  }
  
  @Override
  @Transactional
  public MessageDTO sendImg(MessageDTO messageDTO) {
    chatMapper.insertImgPath(messageDTO);
    return chatMapper.getMsgJustSent(messageDTO.getRoomId());
  }
}
