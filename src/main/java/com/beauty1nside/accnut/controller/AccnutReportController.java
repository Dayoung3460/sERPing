package com.beauty1nside.accnut.controller;

import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

@Log4j2	//log4j 가 안되면 버전높은 log4j2 사용
@Controller
@RequiredArgsConstructor
@RequestMapping("/accnut/report/*")
public class AccnutReportController {

	
	// db커넥션 풀
	@Autowired
	DataSource datasource;
		
	// PDF연결
	@GetMapping("tax/load")
	public void taxLoad(
			@RequestParam("taxNum") int taxNum, 
		    HttpServletRequest request, HttpServletResponse response
			) throws Exception {
		//레포트파일 읽어드림
		InputStream jasperStream = getClass().getResourceAsStream("/reports/accnut/tax.jasper");
		log.info(taxNum);
		
		Map<String, Object> params = new HashMap<>();
	    params.put("p_rgno", taxNum);
		
		//레포트 + 데이터(커넥션) => 레포트파일 환성
		Connection conn = datasource.getConnection();
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperStream, params, conn);
		conn.close();
		
		// 🔹 X-Frame-Options 헤더 제거 (iframe에서 PDF 허용)
	    response.setHeader("X-Frame-Options", "SAMEORIGIN");
	    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	    response.setHeader("Pragma", "no-cache");
	    response.setHeader("Expires", "0");
	
		//pdf 출력
		response.setContentType(MediaType.APPLICATION_PDF_VALUE);
		JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
	}	
	
	
	@GetMapping(value = "tax/down")
	public void taxDown(
								@RequestParam("taxNum") int taxNum,
							    HttpServletResponse response
									    ) throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put("p_rgno", taxNum);
		InputStream jasperStream = getClass().getResourceAsStream("/reports/accnut/tax.jasper");
		
		Connection conn = datasource.getConnection();
	    JasperPrint jasperPrint = JasperFillManager.fillReport(jasperStream, params, conn);
	    conn.close();
		
	    response.setContentType(MediaType.APPLICATION_PDF_VALUE);
	    response.setHeader("Content-Disposition", "attachment; filename=\"accnut_tax_" + taxNum + ".pdf\"");
	    JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
	}
}
