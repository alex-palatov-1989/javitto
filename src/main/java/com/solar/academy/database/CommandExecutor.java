package com.solar.academy.database;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class CommandExecutor implements Runnable{
    final  ExecutorService runner = Executors.newCachedThreadPool();
    final  ExecutorService daemon = Executors.newSingleThreadExecutor();
    public CommandExecutor(Cache repo)
    {
        cache = repo;
        daemon.submit( this );        
    }
    Cache cache;
    volatile boolean running = true;
    /*  =================================================================  */

    public <T> Future<?> put( T value )
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
    final private List<Command> awaiting = new LinkedList<>();
    @Override   @SuppressWarnings("static-access")
    public void run() {

        try {
            while ( running ) {
                if( awaiting.isEmpty() ){
                    Thread.sleep((long) 0.1);
                }
                else try {
                    synchronized (awaiting) {
                        awaiting.forEach(runner::submit);
                        awaiting.clear();
                    }
                } catch (Exception e) { e.printStackTrace();
                } finally {
                    cache.api().commit();
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
            daemon.shutdownNow();
            runner.shutdownNow();
        }
    }
}