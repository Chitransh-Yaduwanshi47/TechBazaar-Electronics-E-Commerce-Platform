package com.codexdrive.electronic.store.services.impl;

import com.codexdrive.electronic.store.exceptions.BadApiRequestException;
import com.codexdrive.electronic.store.services.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Override
    public String uploadFile(MultipartFile file, String path) throws IOException {

        //abc.png
        String originalFilename = file.getOriginalFilename();
        logger.info("Filename : {}", originalFilename);
        String filename = UUID.randomUUID().toString();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileNameWithExtension = filename + extension;
        String fullPathWithFileName = path + fileNameWithExtension;


        logger.info("Absolute path : {}", Paths.get(fullPathWithFileName).toAbsolutePath());
        if (extension.equalsIgnoreCase(".png") || extension.equalsIgnoreCase(".jpg") || extension.equalsIgnoreCase(".jpeg")) {
            //file save
            logger.info("file extension is {}", extension);
            File folder = new File(path);

            if (!folder.exists()) {

                //create the folder
                folder.mkdirs();
            }

            //upload

            Files.copy(file.getInputStream(), Paths.get(fullPathWithFileName));
            return fileNameWithExtension;

        } else {
            throw new BadApiRequestException("File with this" + extension + "not allowed !!");
        }
    }

    @Override
    public InputStream getResource(String path, String name) throws IOException {

        Path fullPath = Paths.get(path, name);

        logger.info("Reading file from : {}", fullPath.toAbsolutePath());

        return Files.newInputStream(fullPath);
    }
}