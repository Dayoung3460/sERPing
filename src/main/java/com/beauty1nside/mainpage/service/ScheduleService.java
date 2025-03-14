package com.beauty1nside.mainpage.service;

import com.beauty1nside.mainpage.dto.ScheduleDTO;

import java.util.List;

public interface ScheduleService {
  int insert(ScheduleDTO dto);
  int deleteSchedule(Long scheduleId);
  int updateSchedule(ScheduleDTO dto);
  List<ScheduleDTO> scheduleList(Long companyNum);
}
