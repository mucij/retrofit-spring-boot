package com.example.githubclient;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.Contributor;
import org.eclipse.egit.github.core.event.DeletePayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.util.List;

@RestController
public class GitHubClientController {
    @Autowired
    private GitHubService githubService;

   
    @RequestMapping(value="/repos", method=RequestMethod.GET)
    @ResponseBody
    public String getRepos() throws IOException {
    	
    	
        return githubService.getRepositories();
    }
    
    @RequestMapping(value="/repos/{owner}/{repo}", method=RequestMethod.GET)
    @ResponseBody
    public String getRepo(@PathVariable("owner") String owner,
            @PathVariable("repo") String repoName) throws IOException {
    	
    	
        return githubService.getRepository(owner, repoName);
        
    }

    @PostMapping("/repos")
    public Repository createRepo(@RequestBody Repository newRepo) throws IOException {
        return githubService.createRepository(newRepo);
    }
    
    @PatchMapping("/repos/{owner}/{repo}")
    public Repository updateRepo(@PathVariable("owner") String owner,
            @PathVariable("repo") String repoName,
            @RequestBody Repository updRepo) throws IOException {
        return githubService.updateRepository(owner, repoName, updRepo);
    }

    @DeleteMapping("/repos/{owner}/{repo}")
    public DeletePayload deleteRepo(
            @PathVariable("owner") String owner,
            @PathVariable("repo") String repoName) throws IOException {
        return githubService.deleteRepository(owner, repoName);
    }
    
}
