package com.example.web.service;

import com.example.web.model.User;
import com.example.web.model.UserWorker;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

@Service
public class EmployeeService {
    @SneakyThrows
    public String getToken(String username , String type){
        URL dist =new URL("http://boot:7070/bootstrapping/add/" + type);
        HttpURLConnection conn = (HttpURLConnection) dist.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("USERNAME" , username);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }

    @SneakyThrows
    public String getWorker(String username){
        String path = "./src/main/resources/user.json";
        ObjectMapper mapper = new ObjectMapper();
        File file = new File(path);
        ObjectNode root = mapper.readValue(file, ObjectNode.class);
        ArrayNode users = (ArrayNode) root.get("user");
        Map<String, String> jsonMap ;
        for(JsonNode node : users) {
            jsonMap = mapper.convertValue(
                    mapper.readTree(node.toString()),
                    new TypeReference<>() {
                    }
            );
            if(jsonMap.get("username").equals(username))
                return jsonMap.get("worker");
        }
        return "Not Found";
    }

    @SneakyThrows
    public void addUser(UserWorker user){
        String path = "./src/main/resources/user.json";
        ObjectMapper mapper = new ObjectMapper();
        File file = new File(path);
        ObjectNode root = mapper.readValue(file, ObjectNode.class);
        ArrayNode users = (ArrayNode) root.get("user");
        String json = mapper.writeValueAsString(user);
        JsonNode jsonNode = mapper.readTree(json);
        users.add(jsonNode);
        FileWriter writer = new FileWriter(path);
        mapper.writeValue(writer, root);
        writer.close();
    }

}
