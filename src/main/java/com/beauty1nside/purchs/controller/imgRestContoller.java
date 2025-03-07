package com.beauty1nside.purchs.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.beauty1nside.common.GridArray;
import com.beauty1nside.common.Paging;
import com.beauty1nside.purchs.dto.ProductDTO;
import com.beauty1nside.purchs.dto.ProductSearchDTO;
import com.beauty1nside.purchs.service.productService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import lombok.extern.log4j.Log4j2;

@Log4j2	//log4j 가 안되면 버전높은 log4j2 사용
@RestController
//@AllArgsConstructor
@RequestMapping("/purchs/rest/*")
public class imgRestContoller {
	@Autowired
	final productService productService;
	
	 private final String imgUrl;
		public imgRestContoller(
				productService productService,
	            @Value("${img.upload}") String imgUrl) {
	        this.productService = productService;
			this.imgUrl = imgUrl;
	    }
		
		
		//이미지 업로드
				@PostMapping("/product/uploadGoodsImages")
				public ResponseEntity<Map<String, Object>> uploadFile(@RequestParam("file") MultipartFile file , ProductDTO dto ) {
				    Map<String, Object> response = new HashMap<>();
			
				    try {
				        if (file.isEmpty()) {
				            response.put("success", false);
				            response.put("message", "업로드할 파일이 없습니다.");
				            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
				        }

				        // 1️ 원본 파일명에서 확장자 추출 (.jpg, .png 등)
				        String originalFileName = file.getOriginalFilename();
				        String fileExtension = "";
				        if (originalFileName != null && originalFileName.contains(".")) {
				            fileExtension = originalFileName.substring(originalFileName.lastIndexOf(".")); 
				        }
				        
				        
				        // 2️ UUID를 이용한 고유한 파일명 생성
				        String uniqueFileName = UUID.randomUUID().toString() + fileExtension;
				        
				        

				        // 3️ 파일 저장 경로 설정
				        Path savePath = Paths.get(imgUrl + uniqueFileName);
				        Files.write(savePath, file.getBytes());
				        //file.transferTo(savePath.toFile());
				        
				       

				        // 4️ 응답 데이터 설정
				        response.put("success", true);
				        response.put("message", "파일 업로드 성공");
				        response.put("fileName", uniqueFileName);  // 고유한 파일명 반환

				        return ResponseEntity.ok(response);
				        
				    } catch (IOException e) {
				        e.printStackTrace();
				        response.put("success", false);
				        response.put("message", "파일 저장 중 오류 발생: " + e.getMessage());
				        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				    } catch (Exception e) {
				        e.printStackTrace();
				        response.put("success", false);
				        response.put("message", "예상치 못한 오류 발생: " + e.getMessage());
				        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				    }
				}
				
			
		
		//상품 리스트 데이터 조회 
		@GetMapping("/product/list")
		public Object productList(@RequestParam(name="perPage",defaultValue="2", required = false) int perPage,
								@RequestParam(name="page", defaultValue = "1" ,required = false) int page,
								 @RequestParam(name="companyNum", required=true) int companyNum,  // ✅ 회사번호 필수
								@ModelAttribute ProductSearchDTO dto, Paging paging) throws JsonMappingException, JsonProcessingException {
			// 회사 번호를 DTO에 설정 (필수)
		    dto.setCompanyNum(companyNum); 
			
			//페이징 유닛 수 
			paging.setPageUnit(perPage);
			paging.setPage(page);
			
			//페이징 조건
			dto.setStart(paging.getFirst());
			dto.setEnd(paging.getLast());
			
			//페이징 처리 
			
			log.info("경로{}",imgUrl);
			
			// 상품 리스트 가져오기
//			    List<ProductDTO> productList = productService.getProductlist(dto);
//			    
//			    if (productList.isEmpty()) {
//			        log.warn("⚠️ 상품 리스트가 비어 있습니다.");
//			    } else {
//			        for (ProductDTO product : productList) {
//			            if (product.getGoodsImage() == null || product.getGoodsImage().isEmpty()) {
//			                product.setGoodsImage(imgUrl + "default.jpg"); // ✅ 기본 이미지 설정
//			                log.warn("⚠️ 상품 이미지 없음, 기본 이미지 적용: {}", product.getGoodsImage());
//			            } else {
//			                product.setGoodsImage(imgUrl + product.getGoodsImage()); // ✅ 최종 경로 설정
//			                log.info("✅ 최종 이미지 경로 확인: {}", product.getGoodsImage());
//			            }
//			        }
//			    }
//

			 
			//grid배열 처리 
			GridArray grid = new GridArray();
			Object result = grid.getArray(paging.getPage(), productService.productcount(dto), productService.getProductlist(dto));
			return result;
		
		}

}
