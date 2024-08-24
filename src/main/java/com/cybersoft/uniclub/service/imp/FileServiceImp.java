package com.cybersoft.uniclub.service.imp;

import com.cybersoft.uniclub.exception.SaveFileException;
import com.cybersoft.uniclub.service.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.plaf.synth.SynthTextAreaUI;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;

@Service
public class FileServiceImp implements FileService {

    @Value("${root.path}")
    private String newRoot;

    @Override
    public void save(MultipartFile file) {

        try {
            Path rootPath = Paths.get(this.newRoot);
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
}
