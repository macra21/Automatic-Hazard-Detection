package org.example.ahd.dto;

import org.springframework.web.multipart.MultipartFile;

public class HazardDetectionRequest {
    private MultipartFile image;
    private String label;
    private Float confidence;
    private Float coord_x;
    private Float coord_y;

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Float getConfidence() {
        return confidence;
    }

    public void setConfidence(Float confidence) {
        this.confidence = confidence;
    }

    public Float getCoord_x() {
        return coord_x;
    }

    public void setCoord_x(Float coord_x) {
        this.coord_x = coord_x;
    }

    public Float getCoord_y() {
        return coord_y;
    }

    public void setCoord_y(Float coord_y) {
        this.coord_y = coord_y;
    }
}
