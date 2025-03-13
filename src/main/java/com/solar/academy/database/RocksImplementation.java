package com.solar.academy.database;

import java.io.*;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


import org.rocksdb.ColumnFamilyDescriptor;
import org.rocksdb.ColumnFamilyHandle;
import org.rocksdb.ReadOptions;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;
import org.rocksdb.RocksIterator;
import org.rocksdb.Snapshot;
import org.rocksdb.WriteBatch;
import org.rocksdb.WriteOptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solar.academy.models.BaseID;

public class RocksImplementation implements IQuerySide, ICommandSide{

    private final HashMap<String,ColumnFamilyHandle> mapped;
    private final RocksDB _db;
    private final WriteOptions writeSinc = new WriteOptions();
    public RocksImplementation(RocksDB db, HashMap<String,ColumnFamilyHandle> map)
    {   
        _db = db; mapped = map; 
        writeSinc.setSync(false);
    }
    /*  =================================================================  */
    private final ObjectMapper objMapper = new ObjectMapper();
    private <T> byte[] toJSON(T obj) {
        try {
            var str = objMapper.writeValueAsString(obj);
            return str.getBytes();
        } catch (JsonProcessingException e) {
            e.printStackTrace();return null;
        }/*
        try(
                var byteOut = new ByteArrayOutputStream();
                var out = new ObjectOutputStream(byteOut)
        ){
            out.writeObject( obj );
            return byteOut.toByteArray();
        } catch (IOException e) { e.printStackTrace();
            return null;
        }*/
    }
    private <T> T fromJSON(byte[] json, Class<T> clazz) {
        try {
            if (json==null)return null;
            var str = new String(json);
            return objMapper.readValue(str, clazz);
        } catch (JsonProcessingException e) {
            e.printStackTrace();return null;
        }/*
        try(
                var byteIn = new ByteArrayInputStream(json);
                var in = new ObjectInputStream(byteIn)
        ){
            return (T) in.readObject();
        } catch (IOException | ClassNotFoundException e) { e.printStackTrace();
            return null;
        }*/
    }

    /*  =================================================================  */
    public String getBytes(String key) throws Exception {
        synchronized (_db) {
            final var k = key.getBytes();
            final var v = _db.get( k );
            return v==null ? null : new String(v);
        }        
    }
    public void putBytes(String key, String val) throws Exception {
        synchronized (_db) {
            _db.put( key.getBytes() , val.getBytes() );
        }   // using 'default' table
    }
    /*  =================================================================  */

    public <T> T get(String id, Class<T> clazz ) {
        synchronized (_db){
            try{
                return fromJSON( _db.get(getTable(clazz.getName()), id.getBytes()), clazz);
            }   catch (RocksDBException e) { e.printStackTrace();
                return null;
            }
        }
    }
    public <T> Object put( T val ){
        byte[] id;
        try {
            var table = getTable(val.getClass().getName());
            synchronized (_db) {
                do id = getID(UUID.randomUUID());
                while (_db.get(id) != null);
                _db.put(id, new byte[]{0});
            }
            if(val instanceof BaseID)
                ((BaseID) val).setKey( setID(id) );
            writeBatch.put( table, id, toJSON(val) );

            return new String(id);
        } catch (Exception e) { e.printStackTrace();
            return e;
        }
    }
    public <T> void putId(String key, T val){
        try{
            var type = val.getClass();
            synchronized (_db){
                if ( type == String.class ) {
                    writeBatch.put( key.getBytes(), ((String) val).getBytes() );
                    // String values sorted to 'default' table and acceptable by getBytes
                } else {
                    var table = getTable( type.getName() );
                    writeBatch.put( table, key.getBytes(), toJSON(val) );
                }
            }
        } catch (Exception e) { e.printStackTrace();
        }
    }
    /*  =================================================================  */    
    class Entry<T>{
        public final Integer key;  public T data;
        public Entry(byte[] k) {  key = setID(k); }
    }
    private Snapshot getSnap(){ synchronized (_db) { return _db.getSnapshot(); }}
    /*  =================================================================  */

    <T> Stream<Entry<T>> getStream(RocksIterator it, Snapshot snap, Class<T> clazz, Set<Integer> skipKeys ){                
        final var all  = skipKeys==null;
        final var skip = skipKeys;
        it.seekToFirst();        

        return Stream.generate(() -> {            
            if( !it.isValid() ) return null;

            final var record = new Entry<T>( it.key() );

            if( all ||  !skip.contains( record.key ) )
            record.data = fromJSON( it.value(), clazz);            
            
            it.next();            
            return record;
        })        
        .takeWhile(Objects::nonNull).filter(e-> e.data!=null )
        .onClose( ()->{ it.close();snap.close(); } );
    }  
    public <T> HashMap<Integer, T> getAll (Class<T> clazz) throws Exception {    
        final var table = getTable(clazz.getName());
        final var snap  = getSnap();
        final var rd = new ReadOptions(); rd.setSnapshot(snap).setAsyncIo(true);
        Stream<Entry<T>> stream;
        synchronized (_db) {
            stream = getStream( _db.newIterator( table, rd ), snap, clazz, null );        
        }
        return (HashMap<Integer, T>) stream.collect(Collectors.toMap(e->e.key, e->e.data));
    }
    public <T> HashMap<Integer , T> equal
        ( 
            Class<T>    clazz,      String          field, 
            Object      value,      Set<Integer>    skipKeys 
        ) throws Exception {
        return filter(
                clazz, field, value, 
                Object::equals,
                skipKeys
            );
    }
    public <T>  HashMap<Integer, T> filter
        ( 
            Class<T> clazz,  String field,  Object value, 
            Predicate pred,  Set<Integer>   skipKeys
        ) throws Exception {

        String method="get"+field.substring(0, 1).toUpperCase() + field.substring(1);        
        final  Method  get;   
        try {   
                get   = clazz.getDeclaredMethod( method );                
        } catch (Exception e) { 
            System.err.println("Not found: "+e.getMessage());
            return new HashMap<>();
        }
        final var table = getTable(clazz.getName());      final var snap  = getSnap();
        final var rd = new ReadOptions();  rd.setSnapshot(snap).setAsyncIo(true);
        Stream<Entry<T>> stream;
        synchronized (_db) {
            stream = getStream( _db.newIterator( table, rd ), snap, clazz, skipKeys );
        }
        return (HashMap<Integer, T>) stream.filter(
            (element) -> {                                                       
                try {    return pred.filter(get.invoke(element.data, (Object[])null), value);                    
                }   catch (Exception e) {   return false;   }                                                         
            }
        ).collect(  Collectors.toMap(e->e.key, e->e.data)    );
    }    
    
    /*  =================================================================  */    

    ColumnFamilyHandle getTable(String classname){
        ColumnFamilyHandle ret  =  null;        
        if( mapped.get(classname)==null )
        try {            
            var type = new ColumnFamilyDescriptor(classname.getBytes());    
            synchronized (_db) {                                
                ret = _db.createColumnFamily ( type );                    
            }
            mapped.put(classname, ret);   
            return ret;                         
        } catch (RocksDBException e) {  e.printStackTrace(); return null;
        } else return mapped.get(classname);        
    }    
    /*  =================================================================  */
    public <T> List<T> getPrivate(String hostId, Class<?> clazz) throws Exception {
        final var snap  = getSnap();
        final var rd = new ReadOptions(); 
        final var table = getTable(clazz.getName());        
        rd.setSnapshot(snap).setAsyncIo(true);

        String idx = null;
        synchronized (_db) {    // associated keys in 'default' table
            var hsh = _db.get(hostId.getBytes());
            if( hsh==null ) return new ArrayList<>();
            idx = new String( hsh );
        }
        final var keys = Arrays.asList( idx.split("::") )
                .stream().map(k-> k.getBytes()).toList();
        final var tables = Stream.generate(()->table).limit(keys.size()).toList();

        List<byte[]> value;
        synchronized (_db) {    value = _db.multiGetAsList( rd, tables, keys ); }

        return  value.stream().filter(Objects::nonNull)
                .map(e-> (T)fromJSON( e, clazz ))
                .filter(Objects::nonNull).toList();
    }
    /*  =================================================================  */
    private final WriteBatch writeBatch = new WriteBatch();
    public void commit(){               
        Thread.ofVirtual().start(
            ()->{
            try{                     
                synchronized (_db) {
                    var commit = new WriteBatch(writeBatch.data());
                    writeBatch.clear();
                    _db.write( writeSinc, commit );                    
                }
            } catch (Exception e) { e.printStackTrace();
            } 
        });             
    }
    public <T> Object addPrivate(String hostId, T value, Class<?> clazz){
            try {
                ColumnFamilyHandle table = getTable(clazz.getName());
                final var id = getNewKey(null);
                if(value instanceof BaseID)
                    ((BaseID) value)
                            .setKey( id )
                            .setHost(hostId);

            synchronized (_db) {
                writeBatch.put(table, id.getBytes(), toJSON(value));
                return id;
            }
            } catch (Exception e) { e.printStackTrace(); 
                return null;
            }                             
    }
    public <T> String getNewKey(T val){
        synchronized (_db) {         
            try {
                byte[] id;      // using "default" table !
                do id  = getID( UUID.randomUUID() );
                while( _db.get( id )!=null );                
                _db.put( id, new byte[]{0} );
                return Integer.toHexString(setID(id));
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }   
        }
    }
    private byte[] getID(UUID rnd){
        return getID( rnd.getMostSignificantBits(), rnd.getLeastSignificantBits() );
    }
    private Integer setID(byte[] arr){
        return  ((arr[0] & 0xFF) << 56) |
                ((arr[1] & 0xFF) << 48) |
                ((arr[2] & 0xFF) << 40) |
                ((arr[3] & 0xFF) << 32) |
                ((arr[4] & 0xFF) << 24) |
                ((arr[5] & 0xFF) << 16) |
                ((arr[6] & 0xFF) << 8)  |
                ( arr[7] & 0xFF);
    }
    private byte[] getID(long val1, long val2){
        return new byte[] {
                (byte) (val1 >> 56),
                (byte) (val1 >> 48),
                (byte) (val1 >> 40),
                (byte) (val1 >> 32),
                (byte) (val2 >> 24),
                (byte) (val2 >> 16),
                (byte) (val2 >> 8),
                (byte)  val2
        };
    }


    /*  =================================================================  */
    public void deleteKey( String key) throws Exception {
        try {
            synchronized (_db){ _db.delete( key.getBytes()); }   
        } catch (RocksDBException e) { e.printStackTrace(); throw e;
        }
    }
    public void delete (String key, Class<?> clazz) throws Exception { 
        try {
            synchronized (_db){ _db.delete( getTable(clazz.getName()), key.getBytes()); }   
        } catch (RocksDBException e) { e.printStackTrace(); throw e;
        }
    }
    
    public void deletePrivate( String hshKey, String key, Class clazz ) throws Exception {
        String hsh;
        try {
            hsh = getBytes(hshKey);
            if( hsh!=null ){
                var upd = Arrays.asList( hsh.split("::") ).parallelStream()
                    .filter( k->!k.contains(key) ).collect(Collectors.joining("::"));
                putBytes(hshKey, upd);
            }            
            var table = getTable( clazz.getName() );
            synchronized (_db){                 
                _db.delete( table, key.getBytes() ); 
            }             
        } catch (RocksDBException e) { e.printStackTrace(); throw e;
        } if( hsh == null )throw new Exception("not found hash table");
    }
}
