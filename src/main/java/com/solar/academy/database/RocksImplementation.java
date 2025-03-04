package com.solar.academy.database;

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

    private final ObjectMapper objMapper = new ObjectMapper();    
    private final HashMap<String,ColumnFamilyHandle> mapped;
    private final RocksDB _db;
    private final WriteOptions writeSinc = new WriteOptions();
    public RocksImplementation(RocksDB db, HashMap<String,ColumnFamilyHandle> map)
    {   
        _db = db; mapped = map; 
        writeSinc.setSync(false);               
    }
    /*  =================================================================  */
    private <T> byte[] toJSON(T obj) {
        try {
            return objMapper.writeValueAsString(obj).getBytes();
        } catch (JsonProcessingException e) {
            e.printStackTrace();return null; 
        }
    }
    private <T> T fromJSON(byte[] json, Class<T> clazz) {
        try {
            if (json==null)return null;
            return objMapper.readValue(new String(json), clazz);
        } catch (JsonProcessingException e) {
            e.printStackTrace();return null; 
        }
    }  
    public String get(String key) throws Exception {
        synchronized (_db) {
            final var k = key.getBytes();
            final var v = _db.get(k);
            return v==null ? null : new String(v);            
        }        
    }
    public void put(String key, String val) throws Exception {
        synchronized (_db) {
            _db.put( key.getBytes() , val.getBytes() );            
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
    public <T> T get(String id, Class<T> clazz ) {
        synchronized (_db){
            try{ 
                return fromJSON( _db.get(getTable(clazz.getName()), id.getBytes()), clazz);
            }   catch (RocksDBException e) { e.printStackTrace(); 
                return null;
            }
        } 
    }
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

        var snap  = getSnap();      
        final var rd = new ReadOptions(); 
        final var table = getTable(clazz.getName());        
        rd.setSnapshot(snap).setAsyncIo(true);
        final var gc = Runtime.getRuntime();
        
        byte[] idx;
        final var start = gc.freeMemory();
        synchronized (_db) {
            idx= _db.get(hostId.getBytes()); if( idx==null ) return new ArrayList<>();
        }   final var hash = (new String(idx)).split("::");
            final var name = clazz.getName();     
            final var keys = Arrays.asList( hash ).parallelStream()
                .filter( k-> k.contains( name ))
                .map(    k-> k.substring(name.length()))
                .map(    k-> k.getBytes() )  .toList();                                
            final var tables = Stream.generate(()->table).limit(keys.size()).toList();
            final List<byte[]> value;
            synchronized (_db) {
                value = _db.multiGetAsList( rd, tables, keys );    
            }   
        final var end = gc.freeMemory();    if( gc.freeMemory()<=start - end )  
        { gc.gc();  System.err.println("\nGC!\n"+ Arrays.toString(Thread.currentThread().getStackTrace())); }
        
        return value.parallelStream().filter(Objects::nonNull)
                    .map( e-> (T) fromJSON(e, clazz) )
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
                byte[] id;
                synchronized (_db) {                
                    do id =  getID( UUID.randomUUID() );
                    while( _db.get(table, id) != null );        
                    
                    final var idx = setID(id);  
                    final var hex = Integer.toHexString(idx);     
                    final var key = hostId + hex;

                    if(value instanceof BaseID)
                    {
                        ((BaseID) value).setKey ( idx );                    
                        ((BaseID) value).setHost(hostId);                    
                    }                    
                    final var data = toJSON( value );

                    writeBatch.put(table, key.getBytes(), data );                        
                    return key;
                }                                                                    
            } catch (Exception e) { e.printStackTrace(); 
                return null;
            }                             
    }
    public <T> Object put( T val ){        
        byte[] id; 
        try {
            var table = getTable(val.getClass().getName());
            synchronized (_db) {
                
                do id  = getID( UUID.randomUUID() );
                while( _db.get( id )!=null );         
                _db.put( id, new byte[]{0} );

                if(val instanceof BaseID)
                ((BaseID) val).setKey(  setID(id) );

                writeBatch.put( table, id, toJSON(val) );                 
            }                                                
            return new String(id);
        } catch (Exception e) { e.printStackTrace(); 
            return e;
        }        
    }
    public <T> void putId(String key, T val){        
        try{
            synchronized (_db){          
                var table= getTable(   val.getClass().getName()  );
                writeBatch.put( table, key.getBytes(), toJSON(val) );
            } 
        } catch (Exception e) { e.printStackTrace(); 
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
    private Integer setID(byte[] byteArray){
        return ((byteArray[0] & 0xFF) << 24) | 
               ((byteArray[1] & 0xFF) << 16) | 
               ((byteArray[2] & 0xFF) << 8)  | 
                (byteArray[3] & 0xFF);
    }
    private byte[] getID(long value){
        return new byte[] {
            (byte) (value >> 24), 
            (byte) (value >> 16),
            (byte) (value >> 8),
            (byte)  value 
        };
    }
    private byte[] getID(UUID rnd){
        var value = rnd.getMostSignificantBits();
        return getID(value);
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
            hsh = get(hshKey);
            if( hsh!=null ){
                var upd = Arrays.asList( hsh.split("::") ).parallelStream()
                    .filter( k->k.contains(key) ).collect(Collectors.joining("::"));
                put(hshKey, upd);
            }            
            var table = getTable( clazz.getName() );
            synchronized (_db){                 
                _db.delete( table, key.getBytes() ); 
            }             
        } catch (RocksDBException e) { e.printStackTrace(); throw e;
        } if( hsh == null )throw new Exception("not found hash table");
    }
}
