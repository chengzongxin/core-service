package com.czx.service.impl;

import com.czx.mapper.FileRecordMapper;
import com.czx.pojo.FileRecord;
import com.czx.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    
    @Autowired
    private FileRecordMapper fileRecordMapper;
    
    @Value("${file.upload-dir:./uploads}")
    private String uploadDir;
    
    @Override
    @Transactional
    public FileRecord uploadFile(MultipartFile file, String username) {
        try {
            // 创建上传目录
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            
            // 生成唯一文件名
            String originalFilename = file.getOriginalFilename();
            String fileExtension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String storedFilename = UUID.randomUUID().toString() + fileExtension;
            
            // 保存文件到磁盘
            Path filePath = uploadPath.resolve(storedFilename);
            Files.copy(file.getInputStream(), filePath);
            
            // 创建文件记录
            FileRecord fileRecord = new FileRecord();
            fileRecord.setOriginalName(originalFilename);
            fileRecord.setStoredName(storedFilename);
            fileRecord.setFilePath(filePath.toString());
            fileRecord.setFileSize(file.getSize());
            fileRecord.setFileType(file.getContentType());
            fileRecord.setUploadedBy(username);
            fileRecord.setUploadTime(LocalDateTime.now());
            
            // 保存到数据库
            fileRecordMapper.insert(fileRecord);
            
            return fileRecord;
            
        } catch (IOException e) {
            throw new RuntimeException("文件上传失败: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<FileRecord> getAllFiles() {
        return fileRecordMapper.findAll();
    }
    
    @Override
    public FileRecord getFileById(Integer id) {
        return fileRecordMapper.findById(id);
    }
    
    @Override
    @Transactional
    public boolean deleteFile(Integer id) {
        FileRecord fileRecord = fileRecordMapper.findById(id);
        if (fileRecord != null) {
            // 删除物理文件
            try {
                Path filePath = Paths.get(fileRecord.getFilePath());
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                // 记录日志但不影响数据库删除
                System.err.println("删除物理文件失败: " + e.getMessage());
            }
            
            // 删除数据库记录
            return fileRecordMapper.deleteById(id) > 0;
        }
        return false;
    }
    
    @Override
    public byte[] downloadFile(Integer id) {
        FileRecord fileRecord = fileRecordMapper.findById(id);
        if (fileRecord != null) {
            try {
                Path filePath = Paths.get(fileRecord.getFilePath());
                return Files.readAllBytes(filePath);
            } catch (IOException e) {
                throw new RuntimeException("文件下载失败: " + e.getMessage(), e);
            }
        }
        throw new RuntimeException("文件不存在");
    }
}
