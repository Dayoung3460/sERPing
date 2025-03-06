package com.beauty1nside.purchs.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.beauty1nside.purchs.dto.ProductDTO;
import com.beauty1nside.purchs.dto.PurchaseDTO;
import com.beauty1nside.purchs.service.productService;
import com.beauty1nside.purchs.service.purchaseService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2	//log4j 가 안되면 버전높은 log4j2 사용
@Controller
@AllArgsConstructor
@RequestMapping("/purchs/*")
public class PurchsSampleController {
	
	private final productService productService;
	private final purchaseService purchaseService;
	
	@ModelAttribute public void setAttributes(Model model) { model.addAttribute("menu", "inventory");}

	//샘플 페이지
	@GetMapping("/")
	public String sample() {
		return "purchs/sample"; // 페이지 출력 
	};
	

	//제품 등록 페이지 이동
	@GetMapping("/goodsRegister")
	public void showRegister(Model model) {
		ProductDTO productDTO = new ProductDTO();
		model.addAttribute("ProductDTO",productDTO);
	}
	
	
	//발주서 등록 페이지 이동 
	@GetMapping("/purchaseRegister")
	public void purchaseRegister(Model model) {
	    PurchaseDTO purchaseDTO = new PurchaseDTO();
	    model.addAttribute("PurchaseDTO", purchaseDTO);
	   
	}
//	@GetMapping("/purchs/purchaseRegister")
//	public String purchaseRegister(Model model) {
//	    System.out.println("✅ purchaseRegister 컨트롤러 실행됨");
//	    return "purchs/purchaseRegister";
//	}
//

	
	
	//발주서 등록 페이지 이동 
	@GetMapping("/goodslist")
	public String goodslist() {
		return "purchs/goodsList";
	}
	
	
	// 메일 페이지 이동
	@GetMapping("/mailsender")
	public String mailtest() {
		return "purchs/mailsender"; // 페이지 출력 
	};
	
	
	//발주 리스트 이동 
    @GetMapping("/purchaseList")
    public String purchaseList() {
        return "purchs/purchaseList"; // templates/purchs/purchaseList.html을 반환
    }
    
    //입고등록 이동 
    @GetMapping("/warehousingRegister")
    public String warehousingRegister() {
        return "purchs/warehousingRegister"; // templates/purchs/purchaseList.html을 반환
    }
    
    //입고리스트이동
    @GetMapping("/warehouseList")
    public String warehousingList() {
        return "purchs/warehouseList"; // templates/purchs/purchaseList.html을 반환
    }
    
  //상품 옵션 상세 정보 조회
    @GetMapping("/info")
    public String getProductDetail(@RequestParam(name="goodsCode") String goodsCode, 
                                   @RequestParam(name="companyNum", required=false, defaultValue="0") int companyNum, 
                                   Model model) {
     

        List<ProductDTO> products = productService.getGoodsOption(goodsCode, companyNum);

        if (!products.isEmpty()) {
            model.addAttribute("product", products.get(0));  // ✅ 공통 상품 정보 (1개만)
            model.addAttribute("options", products); // ✅ 옵션 리스트 (여러 개)
        } else {
            log.warn("⚠️ 조회된 상품 옵션 데이터가 없습니다.");
            model.addAttribute("options", new ArrayList<>());  // 🔴 여기가 누락될 경우 `null`이 전달됨
        }

        return "purchs/goodsModify";
    }
    
    //상품재고조회 페이지 이동 
    @GetMapping("/goodsLots")
    public String goodsNum() {
        return "purchs/goodsLots"; // templates/purchs/goodsLots.html을 반환
    }
    
    
  //발주서 옵션 상세 정보 조회
    @GetMapping("/purchaseModify")
    public String getPurchsDetail(@RequestParam(name="purchaseNum") Long purchaseNum, 
                                   @RequestParam(name="companyNum", required=false, defaultValue="0") int companyNum, 
                                   Model model) {
     

        List<PurchaseDTO> purchaseList = purchaseService.getPurchsinfo(purchaseNum, companyNum);
        
        if (!purchaseList.isEmpty()) {
            PurchaseDTO purchase = purchaseList.get(0);

            // ✅ Java에서 날짜를 변환 (yyyy-MM-dd)
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            if (purchase.getPurchaseDate() != null) {
                String formattedPurchaseDate = dateFormat.format(purchase.getPurchaseDate());
                model.addAttribute("formattedPurchaseDate", formattedPurchaseDate);
            }

            if (purchase.getPurchaseDueDate() != null) {
                String formattedDueDate = dateFormat.format(purchase.getPurchaseDueDate());
                model.addAttribute("formattedPurchaseDueDate", formattedDueDate);
            }

            model.addAttribute("purchase", purchase);
            model.addAttribute("options", purchaseList);
        } else {
            log.warn("⚠️ 조회된 상품 옵션 데이터가 없습니다.");
            model.addAttribute("options", new ArrayList<>());  
        }
        
        return "purchs/purchaseModify";
    }








	
	
	
	
	
}
