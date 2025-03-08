package com.solar.academy.database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class CommandExecutor implements Runnable{
    final  ExecutorService runner = Executors.newCachedThreadPool();
    final  ExecutorService daemon = Executors.newSingleThreadExecutor();    
    boolean running = true;
    public CommandExecutor(Cache repo)
    {
        cache = repo;
        daemon.submit( this );        
    }
    Cache cache;
    /*  =================================================================  */

    public <T> Future<?> put( T value ) throws Exception
    {
        return  addExec( ICommandSide.COMMAND.POST, value );
    }
    public <T> Future<?> put( String key, T value )
    {
        return addExec( ICommandSide.COMMAND.PUT,  key, value );
    }
    public <T> Future<?> addPrivate( String hostId, T value, Class<?> clazz )
    {
        return addExec( ICommandSide.COMMAND.ADD,  hostId, value, clazz );
    }

    Future<Object> addExec(ICommandSide.COMMAND task, Object... args){
        var cmd = new Command( task, args );
        synchronized (awaiting) {
            awaiting.add(cmd);
        }
        return cmd;
    }

    /*  =================================================================  */
    class Command extends CompletableFuture<Object> implements Callable<Object>{
        Object[] args;  ICommandSide.COMMAND method;       

        public Command( ICommandSide.COMMAND mtd, Object... arg )
        {   
            method = mtd; args = arg; exceptionally(  e->{e.printStackTrace(); return  null;} );
        }
        @Override
        public Object call(){
            complete( cache.invoke(method, args) );
            return null;
        }
    }
    /*  =================================================================  */

    final private List<Command> awaiting = new ArrayList<>();       
    @Override   @SuppressWarnings("static-access")
    public void run() {
        try {
            final List<Command> done = new ArrayList<>();
            while( running ){                                
                
                if( awaiting.isEmpty() )    Thread.sleep(0, 999);
                else try {                        
                    Object[] exe;   
                    synchronized (awaiting) {                                           
                        exe = Arrays.copyOf(awaiting.toArray(), 
                            awaiting.size() > cache.cores ? 
                            awaiting.size() / cache.cores : 
                            awaiting.size()
                        );
                    }
                    for( var task : exe ){
                        runner.submit( (Command) task );
                        done.add( (Command) task );
                    }                                                                                            
                } catch (Exception e) { e.printStackTrace(); }                                                              
                finally
                {                                                                                
                    cache.api().commit();                  
                    synchronized (awaiting) {
                        done.forEach(awaiting::remove);         
                    }   done.clear();                                                                                
                }                                
            }            
        } catch(Exception e) { e.printStackTrace();
        } finally {
            synchronized (awaiting) {
                awaiting.forEach(runner::submit);
            }
            cache.api().commit();
            stop();
        }        
    }
    /*  =================================================================  */
    public  void stop(){ 
        running=false;
        try {
            Thread.sleep(999);
            daemon.shutdown();  var main = daemon.awaitTermination(1, TimeUnit.SECONDS);
            runner.shutdown();  var fibs = runner.awaitTermination(1, TimeUnit.SECONDS);
            if(!main)daemon.shutdownNow();
            if(!fibs)runner.shutdownNow();
        } catch (Exception e) {
            daemon.shutdownNow(); runner.shutdownNow();            
        } finally {
            Thread.currentThread().interrupt();
        }
    }
}