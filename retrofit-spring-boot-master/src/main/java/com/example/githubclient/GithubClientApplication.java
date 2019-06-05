package com.example.githubclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;


@SpringBootApplication(exclude = {GsonAutoConfiguration.class})
public class GithubClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(GithubClientApplication.class, args);
	}
}
