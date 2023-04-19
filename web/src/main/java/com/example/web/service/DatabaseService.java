package com.example.web.service;

import com.example.web.model.User;
import jakarta.servlet.http.HttpSession;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@Service
public class DatabaseService {
    private EmployeeService employeeService = new EmployeeService();
    @SneakyThrows
    public String createDatabase(String db_name , HttpSession session){
        User user = (User) session.getAttribute("login");
        String workerId = employeeService.getWorker(user.getUsername());
        if (!Character.isDigit(workerId.charAt(0))) {
            workerId = "0";
        }
        URL dist =new URL("http://w"+workerId+":8080/worker/db/create/database/" + db_name +"/update/true");
        HttpURLConnection conn = (HttpURLConnection) dist.openConnection();
        conn.setRequestMethod("GET");
        System.out.println(user.getUsername());
        System.out.println(user.getToken());
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
    public boolean deleteDatabase(String db_name , HttpSession session){
        User user = (User) session.getAttribute("login");
        String workerId = employeeService.getWorker(user.getUsername());
        if (!Character.isDigit(workerId.charAt(0))) {
            workerId = "0";
        }
        URL dist =new URL("http://w"+workerId+":8080/worker/db/delete/database/" + db_name +"/true/true");
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
        if(response.toString().equalsIgnoreCase("delete-success"))
            return true;
        return false;
    }

    @SneakyThrows
    public String getList(HttpSession session){
        User user = (User) session.getAttribute("login");
        String workerId = employeeService.getWorker(user.getUsername());
        if (!Character.isDigit(workerId.charAt(0))) {
            workerId = "0";
        }
        URL dist =new URL("http://w"+workerId+":8080/worker/database/list");
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

}
