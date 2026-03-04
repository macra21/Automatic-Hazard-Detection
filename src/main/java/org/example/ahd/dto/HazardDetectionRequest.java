package org.example.ahd.dto;

import org.springframework.web.multipart.MultipartFile;

public class HazardDetectionRequest {
    private MultipartFile image;
    private String label;
    private Float confidence;
    private Integer coord_x;
    private Integer coord_y;

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

    public Integer getCoord_x() {
        return coord_x;
    }

    public void setCoord_x(Integer coord_x) {
        this.coord_x = coord_x;
    }

    public Integer getCoord_y() {
        return coord_y;
    }

    public void setCoord_y(Integer coord_y) {
        this.coord_y = coord_y;
    }
}
