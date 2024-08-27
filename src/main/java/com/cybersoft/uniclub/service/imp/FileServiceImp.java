package com.cybersoft.uniclub.service.imp;

import com.cybersoft.uniclub.exception.FileNotFoundException;
import com.cybersoft.uniclub.exception.SaveFileException;
import com.cybersoft.uniclub.service.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.*;

@Service
public class FileServiceImp implements FileService {

    @Value("${root.path}")
    private String root;

    @Override
    public void save(MultipartFile file) {

        try {
            Path rootPath = Paths.get(this.root);
            if (!Files.exists(rootPath)){
                Files.createDirectories(rootPath);
            }
            System.out.println(rootPath);

            //.resolve là thêm dấu /
            Files.copy(file.getInputStream(), rootPath.resolve(file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);

        } catch (Exception e) {
            throw new SaveFileException(e.getMessage());
        }
    }

    @Override
    public Resource load(String filename) {
        try {
            Path rootPath = Paths.get(root);
            Path file = rootPath.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists()){
                return resource;
            } else {
                throw new FileNotFoundException();
            }
        } catch (Exception e) {
            throw new FileNotFoundException(e.getMessage());
        }
    }
}
