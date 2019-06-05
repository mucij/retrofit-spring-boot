package com.example.githubclient;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.AfterClass;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.Contributor;
import com.google.gson.Gson;

@RunWith(SpringRunner.class)
@WebMvcTest
public class GithubClientApplicationTests {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private GitHubService gitHubService;

	@Test
	public void postRepo() throws Exception {

		Gson gson = new Gson();

		Repository repository = new Repository();
		repository.setName("some-super-cool-repo");
		repository.setDescription("This is a test repository created using my GitHub client");
		repository.setHomepage("https://github.com");
		repository.setPrivate(false);
		repository.setHasIssues(true);
		repository.setHasWiki(true);
		
		
		
        given(gitHubService.createRepository(repository)).willReturn(repository);

		MvcResult result = mockMvc.perform(post("/repos")
			.contentType(MediaType.APPLICATION_JSON)
			.content(gson.toJson(repository)))
			.andExpect(status().isOk())
			.andReturn();
		
		String content = result.getResponse().getContentAsString();
		System.out.println("> post json " + content);
		
		

	}
	


}
