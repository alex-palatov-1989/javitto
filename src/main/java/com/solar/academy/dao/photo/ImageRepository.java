package com.solar.academy.dao.photo;


import org.rocksdb.*;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Repository
public class ImageRepository implements AutoCloseable{
    private final String    DB_PATH = "/tmp/image_storage";
    private final Integer   cores   = 4;
    private RocksDB db;
    private Options deflt = new Options();
    @Override public void close(){ db.close(); }

    public ImageRepository()  {
        deflt.setCreateIfMissing(true)
                .setIncreaseParallelism(cores)
                .getEnv().setBackgroundThreads(cores);

        try{    db = RocksDB.open (     deflt, DB_PATH          );
        }catch(RocksDBException e){     e.printStackTrace();    }
    }
    public  List<String> saveImages(List<MultipartFile> images) throws Exception {
        var opt     = new WriteOptions().setSync(false);
        var batch   = new WriteBatch();

        List<String> keys = images.stream().map(
            (data)->{
                String key = null;  byte[] id = null;
                try{
                    do id = UUID.randomUUID().toString().getBytes();
                    while ( db.get(id)!=null );
                    synchronized (db) {
                        db.put( id, new byte[]{0} );
                    }
                    batch.put( id, data.getBytes() );
                    key = new String( id );
                }catch(RocksDBException | IOException e) { e.printStackTrace(); }
                return key;
            }).filter(Objects::nonNull).toList();

        synchronized (db){  db.write(opt, batch);   }
        return keys;
    }
    public  String saveImage(MultipartFile image) throws Exception {
        return saveImages( List.of(image) ).getFirst();
    }
    public  byte[] load(String id) throws Exception{
        try{
            return  db.get( id.getBytes() );
        }catch (RocksDBException e){ e.printStackTrace(); throw e; }
    }
}
