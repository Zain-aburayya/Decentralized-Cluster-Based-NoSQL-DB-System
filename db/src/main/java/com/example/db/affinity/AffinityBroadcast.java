package com.example.db.affinity;

import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class AffinityBroadcast {
    @SneakyThrows
    public String buildDatabase(String db_name , String port){
        String url = "http://" + port + ":8080";
        URL dist = new URL(url + "/worker/db/create/database/" + db_name + "/update/false");
        HttpURLConnection conn = (HttpURLConnection) dist.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("USERNAME" , "");
        conn.setRequestProperty("TOKEN" , "");
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
    public String buildCollection(String db_name,String collection_name , String json , String port){
        String url = "http://" + port + ":8080";
        URL dist = new URL(url + "/worker/db/add/collection/" + db_name + "/" + collection_name +"/update/false");
        HttpURLConnection conn = (HttpURLConnection) dist.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("USERNAME" , "");
        conn.setRequestProperty("TOKEN" , "");
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
    public String buildDocument(String db_name , String collection_name , String json , String port){
        String url = "http://" + port + ":8080";
        URL dist = new URL(url + "/worker/db/add/document/" + db_name + "/" + collection_name +"/update/false");
        HttpURLConnection conn = (HttpURLConnection) dist.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("USERNAME" , "");
        conn.setRequestProperty("TOKEN" , "");
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
    public String deleteCollection(String db_name, String collection_name, String port) {
        String url = "http://" + port + ":8080";
        URL dist = new URL(url + "/worker/db/delete/collection/" + db_name + "/" +collection_name+ "/update/false");
        HttpURLConnection conn = (HttpURLConnection) dist.openConnection();
        conn.setRequestMethod("DELETE");
        conn.setRequestProperty("USERNAME" , "");
        conn.setRequestProperty("TOKEN" , "");
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
    public String deleteDatabase(String db_name, String port) {
        String url = "http://" + port + ":8080";
        URL dist = new URL(url + "/worker/db/delete/database/" + db_name + "/update/false");
        HttpURLConnection conn = (HttpURLConnection) dist.openConnection();
        conn.setRequestMethod("DELETE");
        conn.setRequestProperty("USERNAME" , "");
        conn.setRequestProperty("TOKEN" , "");
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
    public String updateDocument(String db_name, String collection_name, String id
            , String prop, String value, String port) {
        String url = "http://" + port + ":8080";
        URL dist = new URL(url + "/worker/db/update/" + db_name + "/" + collection_name +
                  "/" +id + "/" + prop + "/update/false");
        HttpURLConnection conn = (HttpURLConnection) dist.openConnection();
        conn.setRequestMethod("PUT");
        conn.setRequestProperty("USERNAME" , "");
        conn.setRequestProperty("TOKEN" , "");
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

    @SneakyThrows
    public String deleteDocument(String db_name, String collection_name, String value, String port) {
        String url = "http://" + port + ":8080";
        URL dist = new URL(url + "/worker/db/delete/document/" + db_name + "/" +collection_name + "/" + value + "/update/false");
        HttpURLConnection conn = (HttpURLConnection) dist.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("USERNAME" , "");
        conn.setRequestProperty("TOKEN" , "");
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
