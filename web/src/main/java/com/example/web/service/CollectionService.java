package com.example.web.service;

import com.example.web.model.User;
import jakarta.servlet.http.HttpSession;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class CollectionService {
    private EmployeeService employeeService = new EmployeeService();
    @SneakyThrows
    public String createCollection(String db_name , String collection_name ,String json, HttpSession session){
        User user = (User) session.getAttribute("login");
        String workerId = employeeService.getWorker(user.getUsername());
        if(workerId.charAt(0) < '0' || workerId.charAt(0) > '9')
            workerId = "0";
        URL dist =new URL("http://w"+workerId+":8080/worker/db/add/collection/" + db_name +"/"+collection_name+"/update/true");
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
    public String deleteCollection(String db_name , String collection_name , HttpSession session){
        User user = (User) session.getAttribute("login");
        String workerId = employeeService.getWorker(user.getUsername());
        if(workerId.charAt(0) < '0' || workerId.charAt(0) > '9')
            workerId = "0";
        URL dist =new URL("http://w"+workerId+":8080/worker/db/delete/collection/" + db_name +"/"+collection_name+"/true/true");
        HttpURLConnection conn = (HttpURLConnection) dist.openConnection();
        conn.setRequestMethod("DELETE");
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
    public String getList(String db_name,HttpSession session){
        User user = (User) session.getAttribute("login");
        String workerId = employeeService.getWorker(user.getUsername());
        if(workerId.charAt(0) < '0' || workerId.charAt(0) > '9')
            workerId = "0";
        URL dist =new URL("http://w"+workerId+":8080/worker/collection/list/" + db_name);
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
    public boolean isExist(String db_name , String collection_name, HttpSession session){
        User user = (User)session.getAttribute("login");
        String workerId = employeeService.getWorker(user.getUsername());
        if(workerId.charAt(0) < '0' || workerId.charAt(0) > '9')
            workerId = "0";
        URL dist =new URL("http://w"+workerId+":8080/worker/db/find/" + db_name + "/" + collection_name);
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
        return response.toString().equals("true");
    }
}

