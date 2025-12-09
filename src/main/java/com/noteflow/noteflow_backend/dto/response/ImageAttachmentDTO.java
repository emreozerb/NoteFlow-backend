package com.noteflow.noteflow_backend.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageAttachmentDTO {
    private Long imageId;
    private Long noteId;
    private String fileName;
    private String imageUrl;
    private String mimeType;
    private Long fileSize;
    private Integer displayOrder;
    private LocalDateTime uploadedAt;
}
