package com.example.web.service;

import com.example.web.model.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import lombok.SneakyThrows;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.xml.validation.Schema;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DocumentService {
    private final EmployeeService employeeService = new EmployeeService();
    @SneakyThrows
    public String getList(String db_name ,
                          String collection_name,
                          HttpSession session ){
        User user = (User) session.getAttribute("login");
        String workerId = employeeService.getWorker(user.getUsername());
        if (!Character.isDigit(workerId.charAt(0))) {
            workerId = "0";
        }
        URL dist =new URL("http://w"+workerId+":8080/worker/document/list/" + db_name +"/" + collection_name);
        HttpURLConnection conn = (HttpURLConnection) dist.openConnection();
        conn.setRequestMethod("GET");
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
    public String addDocument(String db_name , String collection_name , Map<String,String> prop,
                              HttpSession session){
        User user = (User) session.getAttribute("login");
        ObjectMapper mapper = new ObjectMapper();
        Map<String,String> jsonMap = new HashMap<>();
        for (Map.Entry<String, String> entry : prop.entrySet()) {
            if(entry.getKey().equals("db_name") || entry.getKey().equals("collection_name")) {
                continue;
            }
            jsonMap.put(entry.getKey() , entry.getValue());
        }
        String json = mapper.writeValueAsString(jsonMap);
        System.out.println("json -> " + json);
        String workerId = employeeService.getWorker(user.getUsername());
        if (!Character.isDigit(workerId.charAt(0))) {
            workerId = "0";
        }
        URL dist =new URL("http://w"+workerId+":8080/worker/db/add/document/" + db_name +"/" + collection_name + "/update/true");
        HttpURLConnection conn = (HttpURLConnection) dist.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("USERNAME" , user.getUsername());
        conn.setRequestProperty("TOKEN" , user.getToken());
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);
        OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
        out.write(json);
        out.close();
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
    public List<String> schemaProp(String db_name , String collection_name, HttpSession session){
        String schema = getSchema(db_name,collection_name,session);
        System.out.println("->" + schema);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode schemaNode = objectMapper.readTree(schema);
        Map<String, Object> propertiesMap = new HashMap<>();
        JsonNode propertiesNode = schemaNode.get("properties");
        if (propertiesNode != null) {
            propertiesNode.fields().forEachRemaining(entry -> {
                String propertyName = entry.getKey();
                JsonNode propertyNode = entry.getValue();
                Map<String, Object> propertyMap = new HashMap<>();
                propertyMap.put("type", propertyNode.get("type").asText());
                // Add any additional property fields here
                propertiesMap.put(propertyName, propertyMap);
            });
        }
        List<String> prop = new ArrayList<>();
        for(Map.Entry<String,Object> entry : propertiesMap.entrySet()){
            prop.add(entry.getKey());
        }
        return prop;
    }

    @SneakyThrows
    private String getSchema(String db_name , String collection_name ,HttpSession session) {
        User user = (User) session.getAttribute("login");
        String workerId = employeeService.getWorker(user.getUsername());
        if (!Character.isDigit(workerId.charAt(0))) {
            workerId = "0";
        }
        URL dist =new URL("http://w"+workerId+":8080/worker/document/schema/" + db_name +"/" + collection_name);
        HttpURLConnection conn = (HttpURLConnection) dist.openConnection();
        conn.setRequestMethod("GET");
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
    public String deleteDocument(String db_name , String collection_name , String id , HttpSession session){
        User user = (User) session.getAttribute("login");
        String workerId = employeeService.getWorker(user.getUsername());
        if (!Character.isDigit(workerId.charAt(0))) {
            workerId = "0";
        }
        URL dist =new URL("http://w"+workerId+":8080/worker/db/delete/document/" +
                db_name +"/" + collection_name + "/" + id + "/update/true");
        HttpURLConnection conn = (HttpURLConnection) dist.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("USERNAME" , user.getUsername());
        conn.setRequestProperty("TOKEN" , user.getToken());
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
    public String updateDocument(String db_name , String collection_name , String id ,
                                 String prop, String value ,  HttpSession session){
        User user = (User) session.getAttribute("login");
        URL dist =new URL("http://w0:8080/worker/db/update/" +
                db_name +"/" + collection_name + "/" + id + "/" + prop + "/update/true");
        HttpURLConnection conn = (HttpURLConnection) dist.openConnection();
        conn.setRequestMethod("PUT");
        conn.setRequestProperty("USERNAME" ,user.getUsername());
        conn.setRequestProperty("TOKEN" , user.getToken());
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);
        OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
        out.write(value);
        out.close();
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
}
