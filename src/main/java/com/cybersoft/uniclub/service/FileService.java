package com.cybersoft.uniclub.service;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface FileService {
    void save(MultipartFile file);
    Resource load(String filename);
}
