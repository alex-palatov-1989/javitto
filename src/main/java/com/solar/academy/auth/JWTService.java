package com.solar.academy.auth;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class JWTService implements AutoCloseable, Runnable{

    final ExecutorService daemon = Executors.newSingleThreadExecutor();
    public JWTService(){
        daemon.submit(this::run);
    }

    @Override public void run(){
        try {
            var builder = new ProcessBuilder("./ws-jwt");
            builder.redirectErrorStream(true);
            Process socket = builder.start();
            String  line;
            try(
                BufferedReader reader =
                    new BufferedReader(
                        new InputStreamReader( socket.getInputStream() )
                    )
            ){
                connection();
                do {
                    line = reader.readLine();
                    System.out.println(line);
                }   while( line != null );
            }
            int exitCode = socket.waitFor();
            System.out.println("Process finished with exit code: " + exitCode);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }   private OAuth ws;

    @Override @PreDestroy
    public void close(){
        try {   ws.end();
            daemon.awaitTermination(1, TimeUnit.SECONDS);
        } catch (Exception e) {     e.printStackTrace();
        }
    }
    void connection(){  ws = new OAuth();   }

    public String get(String token){ return ws.send("get="+token); }
    public long   put(String token)  throws IOException{
        var id  = (int)UUID.randomUUID().getMostSignificantBits();
        var res= ws.send(
                new String("put="+id+"|||"+token)
        );      //System.out.println("JWTService:\n>_"+res);
        if( res.equals("0k") )
            return id; else throw new IOException("err pushing JWT");
    }
    static class OAuth{
        Socket          socket;
        PrintWriter     out;    BufferedReader  in;
        public OAuth(){
            try {
                socket = new Socket("127.0.0.1", 1024);
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {   e.printStackTrace();
            }
        }
        public String send(String message){
            byte[] res = {};
            try
            {
                //System.out.println(message);
                out.println(message);
                res =  in.readLine().getBytes();
            } catch (IOException e) {e.printStackTrace();
            }
            return new String(res);
        }
        public void end(){ out.println("end"); }
    }
}


