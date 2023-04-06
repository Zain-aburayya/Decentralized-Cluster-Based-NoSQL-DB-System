package com.example.bootstrapping;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class BootstrappingApplication {

	public static void main(String[] args) {
		SpringApplication.run(BootstrappingApplication.class, args);
		startUp();
	}
	private static void startUp(){
		addAffinityJson();

	}

	@SneakyThrows
	private static void addAffinityJson(){
		String path = "./src/main/resources/aff.json";
		File file = new File(path);
		Map<String, Object> data = new HashMap<>();
		data.put("affinity", "0");
		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(data);
		FileWriter writer = new FileWriter(file);
		writer.write(json);
		writer.close();
		resetAllWorkersAffinity();
	}

	@SneakyThrows
	private static void resetAllWorkersAffinity(){
		String[] urls = {"http://w0:8080" , "http://w1:8080" ,"http://w2:8080" , "http://w3:8080"};
		for(String url : urls) {
			URL dist = new URL(url + "/worker/reset/affinity");
			HttpURLConnection conn = (HttpURLConnection) dist.openConnection();
			conn.setRequestMethod("GET");
			BufferedReader in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			in.close();
		}
	}

}
