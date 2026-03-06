package org.example.ahd.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.example.ahd.domain.Hazard;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

//TODO contains a HAzard and a MultipartFile(the image)
public class HazardNotificationResourceHandler {
    private Hazard hazard;
    
    // We don't serialize the MultipartFile directly because it's an interface and has streams
    @JsonIgnore
    private MultipartFile image;
    
    // Instead, we send the image content as a Base64 string or byte array
    private String imageContent;
    private String imageName;
    private String imageType;

    public HazardNotificationResourceHandler(Hazard hazard) {
        this.hazard = hazard;
        if (hazard.getImagePath() != null) {
            try {
                Path path = Paths.get(hazard.getImagePath());
                if (Files.exists(path)) {
                    this.image = new LocalMultipartFile(path);
                    this.imageName = this.image.getOriginalFilename();
                    this.imageType = this.image.getContentType();
                    // Convert to Base64 for JSON transmission
                    this.imageContent = Base64.getEncoder().encodeToString(this.image.getBytes());
                }
            } catch (IOException e) {
                // Inexistent file => the image is null
                System.err.println("Cannot load image: " + e.getMessage());
            }
        }
    }

    public HazardNotificationResourceHandler() {}

    public Hazard getHazard() {
        return hazard;
    }

    public void setHazard(Hazard hazard) {
        this.hazard = hazard;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }

    public String getImageContent() {
        return imageContent;
    }

    public void setImageContent(String imageContent) {
        this.imageContent = imageContent;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    private static class LocalMultipartFile implements MultipartFile {
        private final Path path;
        private final byte[] content;
        private final String contentType;

        public LocalMultipartFile(Path path) throws IOException {
            this.path = path;
            this.content = Files.readAllBytes(path);
            String type = Files.probeContentType(path);
            this.contentType = type != null ? type : "application/octet-stream";
        }

        @Override
        public String getName() {
            return path.getFileName().toString();
        }

        @Override
        public String getOriginalFilename() {
            return path.getFileName().toString();
        }

        @Override
        public String getContentType() {
            return contentType;
        }

        @Override
        public boolean isEmpty() {
            return content.length == 0;
        }

        @Override
        public long getSize() {
            return content.length;
        }

        @Override
        public byte[] getBytes() throws IOException {
            return content;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(content);
        }

        @Override
        public void transferTo(File dest) throws IOException, IllegalStateException {
            Files.write(dest.toPath(), content);
        }
    }
}
