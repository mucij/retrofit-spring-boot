package com.example.githubclient;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.Contributor;

import org.eclipse.egit.github.core.event.DeletePayload;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import okhttp3.OkHttpClient;

import java.io.IOException;
import java.util.List;

import java.net.Proxy;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import javax.annotation.PostConstruct;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;

@Service
public class GitHubService implements APIConfiguration {

	private String accessToken;

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	
	public void restoreToken() {
		this.accessToken = "token " + " <access token>";
		
	}
	
	public void incorrectToken() {
		this.accessToken = "token " + " buggytoken";
	}

	private RepositoryInterface service;

	public GitHubService() {
		//Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("test.proxy", 8080));
		CustomResponseInterceptor inter = new CustomResponseInterceptor();
		OkHttpClient client = new OkHttpClient.Builder()
		//.proxy(proxy)
			.addInterceptor(inter)	
			.build();

		Retrofit.Builder builder = new Retrofit.Builder().client(client);

		Retrofit retrofit = builder.baseUrl(API_BASE_URL)
			.addConverterFactory(GsonConverterFactory.create())
			.build();

		service = retrofit.create(RepositoryInterface.class);
		this.accessToken = "token " + " 5af5d6e9695e84f0d5a52f480e403c2b2272abde";

	}

	public String getRepositories() throws IOException {
		Call<List<Repository>> retrofitCall = service.listRepos(accessToken, API_VERSION_SPEC);
		Response<List<Repository>> response = retrofitCall.execute();

		StringBuffer sb = new StringBuffer();

		if (!response.isSuccessful()) {
			throw new IOException(response.errorBody() != null ? response.errorBody()
				.string() : "Unknown error");
		}

		for (Repository r : response.body()) {
			sb.append(r.getName() + " " + r.getDescription());
			List<Contributor> cl = getContributors(r.getName());

			if (cl != null) {
				for (Contributor c : cl) {
					sb.append(c.getContributions());
				}
			} else {
				sb.append(" 0 ");
			}
		}

		return sb.toString();
	}

	public String getRepository(String owner, String repo) throws IOException {
		
		Call<Repository> retrofitCall = service.listRepo(accessToken, API_VERSION_SPEC, owner, repo);

		
		Response<Repository> response = retrofitCall.execute();
		System.out.println("here we are ");
		
		StringBuffer sb = new StringBuffer();
		
		
		if (!response.isSuccessful()) {
			throw new IOException(response.errorBody() != null ? response.errorBody()
				.string() : "Unknown error");
		}

		Repository r = response.body();
		sb.append(r.getName() + " " + r.getDescription());
		List<Contributor> cl = getContributors(r.getName());

		if (cl != null) {
			for (Contributor c : cl) {
				sb.append("\n");
				sb.append(c.getLogin() + " " + c.getName());
			}
		} else {
			sb.append(" There is no contributors ");
		}

		return sb.toString();
	}

	public List<Contributor> getContributors(String repo) throws IOException {

		Response<List<Contributor>> responseCont = null;

		Call<List<Contributor>> contCall = service.listContributors(accessToken, API_VERSION_SPEC, "mucij", repo);
		Response<List<Contributor>> contList = contCall.execute();

		if (!contList.isSuccessful()) {
			throw new IOException(contList.errorBody() != null ? contList.errorBody()
				.string() : "Unknown error");
		}

		return contList.body();
	}

	public Repository createRepository(Repository repo) throws IOException {
		Call<Repository> retrofitCall = service.createRepo(accessToken, API_VERSION_SPEC, JSON_CONTENT_TYPE, repo);

		Response<Repository> response = retrofitCall.execute();

		if (!response.isSuccessful()) {
			throw new IOException(response.errorBody() != null ? response.errorBody()
				.string() : "Unknown error");
		}

		return response.body();
	}

	public Repository updateRepository(String owner, String repoName, Repository repo) throws IOException {

		Call<Repository> retrofitCall = service.patchRepo(accessToken, API_VERSION_SPEC, owner, repoName, repo);

		Response<Repository> response = retrofitCall.execute();

		if (!response.isSuccessful()) {
			throw new IOException(response.errorBody() != null ? response.errorBody()
				.string() : "Unknown error");
		}

		return response.body();
	}

	public DeletePayload deleteRepository(String owner, String repoName) throws IOException {
		Call<DeletePayload> retrofitCall = service.deleteRepo(accessToken, API_VERSION_SPEC, repoName, owner);

		Response<DeletePayload> response = retrofitCall.execute();

		if (!response.isSuccessful()) {
			throw new IOException(response.errorBody() != null ? response.errorBody()
				.string() : "Unknown error");
		}

		return response.body();
	}
}
