package com.solar.academy.services;

import com.solar.academy.dao.photo.ImageRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ImageService {
    ImageRepository db;
    public ResponseEntity<?> get( String id ) throws Exception{
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
    public ResponseEntity<?> post( MultipartFile img )       throws Exception{
        try {
            db.saveImage(img);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body( e.getMessage() );
        }
        return ResponseEntity.ok().build();
    }
    public ResponseEntity<?> post( List<MultipartFile> img ) throws Exception{
        try {
            db.saveImages(img);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body( e.getMessage() );
        }
        return ResponseEntity.ok().build();
    }
}
