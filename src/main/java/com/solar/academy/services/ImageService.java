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
    public ResponseEntity<byte[]> get( String id ) throws Exception{
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "image/jpeg") // Adjust for your image type
                .body( db.load(id) );
    }
    public void post( MultipartFile img ) throws Exception{
        db.saveImage(img);
    }
    public void post( List<MultipartFile> img ) throws Exception{
        db.saveImages(img);
    }
}
