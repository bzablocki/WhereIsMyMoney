package com.bfwg.service.impl;

import com.bfwg.config.FileUploadProperties;
import com.bfwg.exception.FileStorageException;
import com.bfwg.service.FileSystemStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Service
public class FileSystemStorageImpl implements FileSystemStorage {
    private final Path dirLocation;

    @Autowired
    public FileSystemStorageImpl(FileUploadProperties fileUploadProperties) {
        this.dirLocation = Paths.get(fileUploadProperties.getLocation())
                .toAbsolutePath()
                .normalize();
    }

    @Override
    @PostConstruct
    public void init() {
        // TODO Auto-generated method stub
        try {
            Files.createDirectories(this.dirLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create upload dir!");
        }
    }

    private static String getDateTime() {
        String timePattern = "yyyyMMddHHmmss";
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(timePattern);
        LocalDateTime now = LocalDateTime.now();
        return dateTimeFormatter.format(now);
    }

    @Override
    public String saveFile(Long userId, MultipartFile file) {
        // TODO Auto-generated method stub
        try {
            String fileName = file.getOriginalFilename();
            if (fileName == null) {
                fileName = "";
            }
            fileName = getDateTime() + "_" + fileName;
            Path destination = Paths.get(this.dirLocation.toString(), String.valueOf(userId));
            File dir = new File(destination.toString());
            if (!dir.exists()) {
                dir.mkdirs();
            }

            Path dfile = destination.resolve(fileName);
            Files.copy(file.getInputStream(), dfile, StandardCopyOption.REPLACE_EXISTING);
            return dfile.toString();

        } catch (Exception e) {
            throw new FileStorageException("Could not upload file");
        }
    }
}
