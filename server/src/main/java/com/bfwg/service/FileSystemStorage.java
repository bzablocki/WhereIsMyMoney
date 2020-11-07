package com.bfwg.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileSystemStorage {
    void init();
    String saveFile(Long userId, MultipartFile file);
//    Resource loadFile(String fileName);
}
