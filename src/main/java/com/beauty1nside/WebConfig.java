package com.beauty1nside;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
  
   @Value("${img.uploadpath}") String imgUrl;
  
  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/image/**")
      .addResourceLocations("file://"+imgUrl) // 업로드 경로 설정
      .setCacheControl(CacheControl.noCache()); // 캐시 없이 즉시 반영

  }
}
