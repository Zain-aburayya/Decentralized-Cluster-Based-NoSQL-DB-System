package com.example.db.authentication;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthenticationService {
    @SneakyThrows
    public boolean authenticateUser(String username , String token){
        String path = "./src/main/resources/user.json";
        ObjectMapper mapper = new ObjectMapper();
        File file = new File(path);
        ObjectNode root = mapper.readValue(file, ObjectNode.class);
        ArrayNode users = (ArrayNode) root.get("user");
        Map<String, String> jsonMap = new HashMap<>();
        for(JsonNode node : users) {
            jsonMap = mapper.convertValue(
                    mapper.readTree(node.toString()),
                    new TypeReference<>() {
                    }
            );
            if(jsonMap.get("username").equals(username) &&
               jsonMap.get("token").equals(token))
                return true;
        }
        return false;
    }

    @SneakyThrows
    public boolean authenticateAdmin(String username , String token){
        String path = "./src/main/resources/admin.json";
        ObjectMapper mapper = new ObjectMapper();
        File file = new File(path);
        ObjectNode root = mapper.readValue(file, ObjectNode.class);
        ArrayNode users = (ArrayNode) root.get("admin");
        Map<String, String> jsonMap = new HashMap<>();
        for(JsonNode node : users) {
            jsonMap = mapper.convertValue(
                    mapper.readTree(node.toString()),
                    new TypeReference<>() {
                    }
            );
            if(jsonMap.get("username").equals(username) &&
                    jsonMap.get("token").equals(token))
                return true;
        }
        return false;
    }

    @SneakyThrows
    public void addUser(AuthenticationModel model){
        String path = "./src/main/resources/user.json";
        ObjectMapper mapper = new ObjectMapper();
        File file = new File(path);
        ObjectNode root = mapper.readValue(file, ObjectNode.class);
        ArrayNode users = (ArrayNode) root.get("user");
        String json = mapper.writeValueAsString(model);
        JsonNode jsonNode = mapper.readTree(json);
        users.add(jsonNode);
        FileWriter writer = new FileWriter(path);
        mapper.writeValue(writer, root);
        writer.close();
    }

    @SneakyThrows
    public void addAdmin(AuthenticationModel model) {
        String path = "./src/main/resources/admin.json";
        ObjectMapper mapper = new ObjectMapper();
        File file = new File(path);
        ObjectNode root = mapper.readValue(file, ObjectNode.class);
        ArrayNode users = (ArrayNode) root.get("admin");
        String json = mapper.writeValueAsString(model);
        JsonNode jsonNode = mapper.readTree(json);
        users.add(jsonNode);
        FileWriter writer = new FileWriter(path);
        mapper.writeValue(writer, root);
        writer.close();
    }

    @SneakyThrows
    public boolean authenticateAdmin(String username) {
        String path = "./src/main/resources/admin.json";
        ObjectMapper mapper = new ObjectMapper();
        File file = new File(path);
        ObjectNode root = mapper.readValue(file, ObjectNode.class);
        ArrayNode users = (ArrayNode) root.get("admin");
        Map<String, String> jsonMap = new HashMap<>();
        for(JsonNode node : users) {
            jsonMap = mapper.convertValue(
                    mapper.readTree(node.toString()),
                    new TypeReference<>() {
                    }
            );
            if(jsonMap.get("username").equals(username))
                return true;
        }
        return false;
    }
}
