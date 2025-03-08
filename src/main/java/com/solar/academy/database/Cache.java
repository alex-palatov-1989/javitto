package com.solar.academy.database;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solar.academy.models.posts.UserPost;
import lombok.Getter;

import org.rocksdb.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Cache implements AutoCloseable{
    @Override public void close(){
        db.close();
        mapped.values().forEach(ColumnFamilyHandle::close);
        executor.stop(); memory = null;
    }
    private final String         DB_PATH     = "/tmp/rocksdb";
    private final Options        deflt       = new Options();
    private final DBOptions      dbopts      = new DBOptions();      
    
    @Getter private RocksDB db;  
    @Getter private final HashMap<String,ColumnFamilyHandle> mapped = new HashMap<>();
    
    static  final int  cores = Runtime.getRuntime().availableProcessors();
    static  final long cache = 1024*1024 * 512;

    static {   RocksDB.loadLibrary();      }        
    static     LRUCache memory = new LRUCache(cache);

    Cache(){
        List<ColumnFamilyDescriptor>  types  = new ArrayList<>();    
        List<ColumnFamilyHandle>      tables = new ArrayList<>();

        deflt.setCreateIfMissing(true);
        dbopts
            .setAllowConcurrentMemtableWrite(true)
            .setEnablePipelinedWrite(true)
            .setCreateMissingColumnFamilies(true)
            .setCreateIfMissing(true)
            
            .setRowCache    ( memory )
            .setBytesPerSync(cache *4)
            .setWritableFileMaxBufferSize   (cache)
            .setRandomAccessMaxBufferSize   (cache)                 
            .setIncreaseParallelism         (cores*2)            
            .getEnv().setBackgroundThreads  (cores, Priority.HIGH) ;

        try {                                    
            RocksDB.listColumnFamilies(deflt, DB_PATH).forEach(
                name->types.add(new ColumnFamilyDescriptor(name))
            );                       
            types.add(new ColumnFamilyDescriptor("default".getBytes()));             
            db = RocksDB.open(
                dbopts, DB_PATH,
                types,  tables
            );
            IntStream.range(0, tables.size()).forEach(
                i-> mapped.put( new String(types.get(i).getName()), tables.get(i) )
            );

            api      = new RocksImplementation(db, mapped);    
            commands = ( HashMap< String,Method > )
                Stream.of(ICommandSide.class.getDeclaredMethods())            
                    .collect(Collectors.toMap(Method::getName, m->m));

            executor = new CommandExecutor(this);
        }   catch (RocksDBException e) { e.printStackTrace();
        }
    }    

    public Object invoke(ICommandSide.COMMAND method, Object... args) {
        try {            
            return commands.get(method.getMtdName()).invoke(api, args);
        } catch (Exception e) {
            System.out.println("Failed to invoke method: " + method);
            e.printStackTrace(); return null;
        }
    }
    /*  =======================================  */
        public  RocksImplementation      api(){ return api; }
        private RocksImplementation      api;

        public  CommandExecutor          executor(){ return executor; }
        private CommandExecutor          executor;
        private HashMap< String,Method > commands;    
    /*  =======================================  */


    public static void main(String[] args) {
        try {
            var mapper = new ObjectMapper();
            var userP  = new UserPost();
            System.out.println(mapper.writeValueAsString(userP));
        } catch (JsonProcessingException e){ e.printStackTrace();}



        /*
        try (Cache db = new Cache();){
            var dao = new BaseDAO(){
                @Override
                public Class<?> dataclass(){ return TMP.class; }
            };
            var i=0;    var N = 33;     String key = null;
            List <String> keys = new ArrayList<>();
            List <TMP> el = new ArrayList<>();
            var  foo = new  TMP();

            var start = System.currentTimeMillis();
            while( i++<N )
            {
                var tmp = new TMP();
                tmp.linked = new ArrayList<>();
                int cnt = 0;
                do tmp.linked.add( foo );
                while( ++cnt<N );
                key = dao.create(tmp, db);
                keys.add(key);
            }
            var end = System.currentTimeMillis();

            System.out.println( (end-start)      + " ms");
            System.out.println( ((end-start)/N)  + " ms/rec" );

            start = System.currentTimeMillis();
            keys.parallelStream().map(
                    (e)-> {
                        final var k = (String) e;
                        return Thread.ofVirtual().start(
                                ()->{
                                    try {
                                        TMP v = dao.read(k, db);
                                        el.add(v);
                                    } catch (Exception err) { err.printStackTrace();
                                    }
                                }
                        );
                    }
            ).forEach(t -> {
                try {   t.join();
                } catch (InterruptedException e1) { e1.printStackTrace();
                }
            });
            end = System.currentTimeMillis();
            var time = (end-start);

            System.err.println(  (  time  )   +" ms read count:"+ N);
            System.err.println(  ( (time)/N ) +" ms read each");
            System.out.println("\n\t\tEND");
        } catch (Exception e) { e.printStackTrace();
        }
        */
    }
}

