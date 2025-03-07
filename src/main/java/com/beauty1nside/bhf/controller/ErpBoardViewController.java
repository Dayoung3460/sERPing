package com.beauty1nside.bhf.controller;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Log4j2
@Controller
@AllArgsConstructor
@RequestMapping("/bhf/*")
public class ErpBoardViewController {
  //게시판 페이지
  @GetMapping("/erpBoard")
  public String erpBoard() {
    return "bhf/erpBoard";
  };
  
  //게시판 상세 페이지
  @GetMapping("/erpBoardDTL")
  public String erpBoardDTL(@RequestParam("boardId") int boardId, Model model) {
    
    model.addAttribute("boardId",boardId);
    return "bhf/erpBoardDTL";
  };
  
  //게시판 등록 페이지
  @GetMapping("/erpBoardInsert")
  public String erpBoardInsert() {
    return "bhf/erpBoardInsert";
  };
  
  //게시판 수정 페이지
  @GetMapping("/erpBoardModify")
  public String erpBoardModify(@RequestParam("boardId") int boardId, Model model) {
    
    model.addAttribute("boardId",boardId);
    return "bhf/erpBoardModify";
  };
}
