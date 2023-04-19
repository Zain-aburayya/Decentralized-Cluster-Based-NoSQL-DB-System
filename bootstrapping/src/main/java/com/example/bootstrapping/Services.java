package com.example.bootstrapping;

import com.example.bootstrapping.affinity.Affinity;
import com.example.bootstrapping.affinity.AuthenticationModel;
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
public class Services {
    public void setWorkerUser(String username , String token){
        String workerId = Affinity.getInstance().getValue().toString();
        AuthenticationModel model = new AuthenticationModel(username,token,workerId);
        addUserToJson(model);
    }

    public void setWorkerAdmin(String username , String token){
        String workerId = Affinity.getInstance().getValue().toString();
        AuthenticationModel model = new AuthenticationModel(username,token,workerId);
        addAdminToJson(model);
    }

    @SneakyThrows
    private void addUserToJson(AuthenticationModel model){
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
    private void addAdminToJson(AuthenticationModel model){
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
        Affinity.getInstance().updateAffinity();
    }

    @SneakyThrows
    public boolean isExistUser(String username){
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
            System.out.println(jsonMap.get("username"));
            if(jsonMap.get("username").equals(username))
                return true;
        }
        return false;
    }

    @SneakyThrows
    public boolean isExistAdmin(String username){
        String path = "./src/main/resources/admin.json";
        ObjectMapper mapper = new ObjectMapper();
        File file = new File(path);
        ObjectNode root = mapper.readValue(file, ObjectNode.class);
        ArrayNode users = (ArrayNode) root.get("admin");
        Map<String, String> jsonMap ;
        for(JsonNode node : users) {
            jsonMap = mapper.convertValue(
                    mapper.readTree(node.toString()),
                    new TypeReference<>() {
                    }
            );
            System.out.println(jsonMap.get("username"));
            if(jsonMap.get("username").equals(username))
                return true;
        }
        return false;
    }

    @SneakyThrows
    public void sendToWorkerUser(String username, String token) {
        String port = "w" + Affinity.getInstance().getValue().toString();
        System.out.println(Affinity.getInstance().getValue().toString());
        String url = "http://"+port+":8080";
        URL dist =new URL(url + "/add/user");
        HttpURLConnection conn = (HttpURLConnection) dist.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("USERNAME" , username);
        conn.setRequestProperty("TOKEN" , token);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));
        in.close();
    }

    @SneakyThrows
    public void sendToWeb(String username) {
        URL dist =new URL("http://web:9090/add/user");
        HttpURLConnection conn = (HttpURLConnection) dist.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("USERNAME" , username);
        conn.setRequestProperty("WORKER" , Affinity.getInstance().getValue().toString());
        BufferedReader in = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));
        in.close();
        Affinity.getInstance().updateAffinity();
    }

    @SneakyThrows
    public void sendToWorkerAdmin(String username, String token) {
        String[] urls = {"http://w0:8080" , "http://w1:8080" , "http://w2:8080" , "http://w3:8080"};
        for(String url : urls) {
            URL dist = new URL(url + "/add/admin");
            HttpURLConnection conn = (HttpURLConnection) dist.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("USERNAME", username);
            conn.setRequestProperty("TOKEN", token);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            in.close();
        }
    }
}
