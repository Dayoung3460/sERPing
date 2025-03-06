package com.beauty1nside.common;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class FileConfig {
  @Value("${img.upload}")
  private String upload;
}
