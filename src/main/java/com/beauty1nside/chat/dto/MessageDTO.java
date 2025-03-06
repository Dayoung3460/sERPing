package com.beauty1nside.chat.dto;

import lombok.Data;

import java.util.Date;

@Data
public class MessageDTO {
  private Long msgId;
  private Long roomId;
  private Long employeeNum;
  private String employeeName;
  private String msgContent;
  private String imgPath;
  private Date sendDate;
}
