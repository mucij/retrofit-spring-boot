package com.example.githubclient;

import okhttp3.Interceptor;
import okhttp3.Interceptor.Chain;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.Response.Builder;
import okhttp3.MediaType;

import java.io.IOException;

import org.eclipse.egit.github.core.Repository;

import com.google.gson.Gson;
import java.io.File;
import java.nio.file.Files;




public class CustomResponseInterceptor implements Interceptor {

    
    @Override 
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
      
        Response response = chain.proceed(request);
        
      
        
        if (request.method().equals("GET") 
        		&& response.code() > 400 ) {
        	
        
        	
        	File file = new File(
        			getClass().getClassLoader().getResource("defaultRepo.txt").getFile()
        		);
        	String content = new String(Files.readAllBytes(file.toPath()));
        	
    		
    		MediaType mt = response.body().contentType();
        	ResponseBody body = ResponseBody.create(mt, content);
        	Response r = response.newBuilder()
        			.body(body)
        			.message("OK")
        			.code(400)
        			.build();

            return r;
            
            
        }
        
        
        return response;
    }
    
}