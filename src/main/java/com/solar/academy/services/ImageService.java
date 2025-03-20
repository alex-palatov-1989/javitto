package com.solar.academy.services;

import com.solar.academy.dao.photo.ImageRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@Service @AllArgsConstructor
public class ImageService {

    ImageRepository db;

    public ResponseEntity<?> get( String id ) {
        try{
            var image = db.load(id);
            return image == null ?
            ResponseEntity.notFound().build() :
            ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "image/jpeg") // Adjust for your image type
                    .body( image );
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body( e.getMessage() );
        }
    }
    public ResponseEntity<?> post( MultipartFile img ) {
        String key = null;
        try {
            key = db.saveImage(img);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body( e.getMessage() );
        }
        if(Objects.isNull(key)) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok().body( key );
    }
    public ResponseEntity<?> post( List<MultipartFile> img ) {
        try {
            db.saveImages(img);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body( e.getMessage() );
        }
        return ResponseEntity.ok().build();
    }
}
