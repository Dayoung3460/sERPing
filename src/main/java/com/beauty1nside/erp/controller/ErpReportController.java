package com.beauty1nside.erp.controller;

import java.io.FileInputStream;
import java.io.IOException;
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
import org.springframework.web.servlet.ModelAndView;

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
@RequestMapping("/erp/report/*")
public class ErpReportController {

	// db커넥션 풀
	@Autowired
	DataSource datasource;

//	// 파일 다운로드
//	@GetMapping("fileDown")
//	public void fileDown(HttpServletResponse response) throws IOException {
//		FileInputStream fi = new FileInputStream("c:/Temp/data.txt");
//		int readByte;
//		// 파일 다운로드 하게 해줌
//		response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
//		// 파일명을 변경해줌
//		response.setHeader("Content-Disposition", "attachment; filename=" + "data.txt");
//		while ((readByte = fi.read()) != -1) {
//			response.getOutputStream().write(readByte);
//		}
//		fi.close();
//	}

	// PDF연결
	@GetMapping("report")
	public void report(
			@RequestParam("companynum") int companynum, 
		    @RequestParam("subscriptionnum") int subscriptionnum,
		    HttpServletRequest request, HttpServletResponse response
			) throws Exception {
		//레포트파일 읽어드림
		InputStream jasperStream = getClass().getResourceAsStream("/reports/erp/transactionStatement.jasper");
		
		//http://localhost:81/erp/report/report?companynum=1&subscriptionnum=68
		
		log.info(companynum);
		
		Map<String, Object> params = new HashMap<>();
	    params.put("p_companynum", companynum);
	    params.put("p_subscriptionnum", subscriptionnum);
		
		//레포트 + 데이터(커넥션) => 레포트파일 환성
		Connection conn = datasource.getConnection();
		//JasperPrint jasperPrint = JasperFillManager.fillReport(jasperStream, null, conn); 
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperStream, params, conn);
		conn.close();

		//pdf 출력
		response.setContentType(MediaType.APPLICATION_PDF_VALUE);
		JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
	}
	
	//common 패키지에 만들어놓고 호출해서  파일명만 넘겨주면 알아서 처리하게 할 수 도 있다.
	@GetMapping(value = "pdfView")
	public ModelAndView pdfview() throws Exception {
		return new ModelAndView("pdfView", "filename", "/reports/erp/transactionStatement.jasper");
	}
	
//	@GetMapping(value = "pdfViewDown")
//	public ModelAndView pdfviewDown() throws Exception {
//		return new ModelAndView("pdfViewDown", "filename", "/reports/erp/transactionStatement.jasper");
//	}

}
