package com.noteflow.noteflow_backend.mapper;

import com.noteflow.noteflow_backend.dto.response.ImageAttachmentDTO;
import com.noteflow.noteflow_backend.model.ImageAttachment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ImageAttachmentMapper {

    public ImageAttachmentDTO toDTO(ImageAttachment imageAttachment, String baseUrl) {
        if (imageAttachment == null) {
            return null;
        }

        String imageUrl = baseUrl + "/api/notes/" + imageAttachment.getNote().getNoteId()
                + "/images/" + imageAttachment.getImageId();

        return new ImageAttachmentDTO(
                imageAttachment.getImageId(),
                imageAttachment.getNote().getNoteId(),
                imageAttachment.getFileName(),
                imageUrl,
                imageAttachment.getMimeType(),
                imageAttachment.getFileSize(),
                imageAttachment.getDisplayOrder(),
                imageAttachment.getUploadedAt()
        );
    }

    public List<ImageAttachmentDTO> toDTOList(List<ImageAttachment> imageAttachments, String baseUrl) {
        return imageAttachments.stream()
                .map(img -> toDTO(img, baseUrl))
                .collect(Collectors.toList());
    }

}
