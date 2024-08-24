package com.cybersoft.uniclub.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface FileService {
    public void save(MultipartFile file);

}
