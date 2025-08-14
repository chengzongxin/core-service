package com.czx.pojo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class FileRecord {
    private Integer id;
    private String originalName;
    private String storedName;
    private String filePath;
    private Long fileSize;
    private String fileType;
    private String uploadedBy;
    private LocalDateTime uploadTime;
}
