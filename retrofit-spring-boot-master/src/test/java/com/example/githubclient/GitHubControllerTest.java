package com.example.githubclient;


import static org.junit.Assert.assertTrue;

import org.eclipse.egit.github.core.Repository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Before;
import org.junit.After;

import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import java.net.Proxy;
import java.net.InetSocketAddress;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import com.google.gson.Gson;



@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class GitHubControllerTest {

    @LocalServerPort
    private int port;
    
    @Autowired
    ApplicationContext context;

    TestRestTemplate restTemplate = new TestRestTemplate();
    
    HttpHeaders headers = new HttpHeaders();
    
    
    @Before
    public void CreateRepo() throws Exception {
    	
    	System.out.println("Before CreateRepo");
    	headers.setContentType(MediaType.APPLICATION_JSON);
    	
       
        
        Gson gson = new Gson();

		Repository repository = new Repository();
		repository.setName("some-super-cool-repo");
		repository.setDescription("This is a test repository created using my GitHub client");
		repository.setHomepage("https://github.com");
		repository.setPrivate(false);
		repository.setHasIssues(true);
		repository.setHasWiki(true);
		
		 HttpEntity<String> entity = new HttpEntity<String>(gson.toJson(repository), headers);

		
        ResponseEntity<Repository> response = restTemplate.exchange(
          createURLWithPort("/repos"), HttpMethod.POST, entity, Repository.class);
       
        
        assertTrue(response.getBody().getName().equals("some-super-cool-repo"));
        assertTrue(response.getStatusCode() == HttpStatus.OK );
    }    
	
    
    @Test
    public void testGetRepos() throws Exception {
    	System.out.println("Test testGetRepos");
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
          createURLWithPort("/repos"), HttpMethod.GET, entity, String.class);

        System.out.println(response.getBody());
        assertTrue(response.getStatusCode() == HttpStatus.OK);
    }
    
    
    @Test
    public void testGetReposErr() throws Exception {
    	System.out.println("Test testGetRepos");
    	
    	GitHubService ghs = (GitHubService)context.getBean("gitHubService");
    	if(ghs != null)
    		ghs.incorrectToken();
    	
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
          createURLWithPort("/repos/mucij/some-super-cool-repo"), HttpMethod.GET, entity, String.class);
        if(ghs != null)
        	ghs.restoreToken();
        
        assertTrue(response.getStatusCode() == HttpStatus.OK);
    }
    
   
    
    
    @Test
    public void testGetRepo() throws Exception {
    	System.out.println("Test testGetRepo");
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
          createURLWithPort("/repos/mucij/some-super-cool-repo"), HttpMethod.GET, entity, String.class);

        System.out.println(response.getBody());
        assertTrue(response.getStatusCode() == HttpStatus.OK);
    }
    
    @Test
    public void testPatchRepo() throws Exception {
    	System.out.println("Test testPatchRepo");
    	
    	headers.setContentType(MediaType.APPLICATION_JSON);
    	Gson gson = new Gson();

		Repository repository = new Repository();
		repository.setName("some-super-cool-repo");
		repository.setHomepage("https://github.net");
		
		HttpEntity<String> entity = new HttpEntity<String>(gson.toJson(repository), headers);


        ResponseEntity<Repository> response = restTemplate.exchange(
          createURLWithPort("/repos/mucij/some-super-cool-repo"), HttpMethod.PATCH, entity, Repository.class);

        assertTrue(response.getBody().getHomepage().equals("https://github.net"));
        assertTrue(response.getStatusCode() == HttpStatus.OK);
    }
    
    
    @After
    public void DeleteRepos() throws Exception {
    	System.out.println("After DeleteRepos");
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
          createURLWithPort("/repos/mucij/some-super-cool-repo"), HttpMethod.DELETE, entity, String.class);
        
        
        assertTrue(response.getStatusCode() == HttpStatus.OK);
    }
    
    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
    
 
}