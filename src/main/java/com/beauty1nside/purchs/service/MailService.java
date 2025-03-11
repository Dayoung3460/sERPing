package com.beauty1nside.purchs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.mail.internet.MimeMessage;

@Service
public class MailService {

	  @Autowired 
	  private JavaMailSender mailSender;
	  
	  public void sendMailWithAttachment( String fromEmail, String toEmail, String subject, String content, MultipartFile file) throws Exception { 
		// ✅ 1) 수신자 값 확인
	        if (toEmail == null || toEmail.trim().isEmpty()) {
	            throw new IllegalArgumentException("📌 오류: 메일 수신자가 없습니다.");
	        }
	        
        // ✅ 2) MimeMessage 생성
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        // ✅ 3) 메일 정보 설정
        helper.setFrom(fromEmail);
        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(content, true);  // HTML 사용 가능

        // ✅ 4) 첨부파일 처리
        if (file != null && !file.isEmpty()) {
            String filename = file.getOriginalFilename();
            byte[] fileBytes = file.getBytes();
            helper.addAttachment(filename, new ByteArrayResource(fileBytes));
        }

        // ✅ 5) 메일 전송
        mailSender.send(message);
        //System.out.println("✅ 메일 전송 완료: " + toEmail);
		  
		  
		
	}
	 
}
