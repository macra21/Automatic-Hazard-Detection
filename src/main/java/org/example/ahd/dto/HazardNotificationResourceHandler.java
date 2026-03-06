package org.example.ahd.dto;

import org.example.ahd.domain.Hazard;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

//TODO contains a HAzard and a MultipartFile(the image)
public class HazardNotificationResourceHandler {
    private Hazard hazard;
    private MultipartFile image;

    public HazardNotificationResourceHandler(Hazard hazard) {
        this.hazard = hazard;
        if (hazard.getImagePath() != null) {
            try {
                this.image = new LocalMultipartFile(Paths.get(hazard.getImagePath()));
            } catch (IOException e) {
                // În caz de eroare (fișier inexistent), imaginea rămâne null sau puteți loga eroarea
                System.err.println("Nu s-a putut încărca imaginea: " + e.getMessage());
            }
        }
    }

    public HazardNotificationResourceHandler(Hazard hazard, MultipartFile image){
        this.hazard = hazard;
        this.image = image;
    }

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

    // Implementare simplă pentru MultipartFile pentru a încărca fișiere locale
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
