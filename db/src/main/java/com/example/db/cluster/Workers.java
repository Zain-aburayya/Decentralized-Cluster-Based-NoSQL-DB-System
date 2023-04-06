package com.example.db.cluster;

import com.example.db.affinity.Affinity;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class Workers {
    String []urls = {"http://w0:8080","http://w1:8080" , "http://w2:8080" , "http://w3:8080"};

    @SneakyThrows
    public boolean checkWorkersDatabase(String db_name){
        for(String url : urls) {
            if(("http://w" +Affinity.getInstance().getValue() + ":8080").equalsIgnoreCase(url))
                continue;
            URL dist = new URL(url + "/worker/db/find/" + db_name);
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
            if(response.toString().equals("true"))
                return true;
        }
        return false;
    }

    @SneakyThrows
    public void buildDatabase(String db_name){
        for(String url : urls) {
            if(("http://w" +Affinity.getInstance().getValue() + ":8080").equalsIgnoreCase(url))
                continue;
            URL dist = new URL(url + "/worker/db/create/database/" + db_name + "/noupdate/false");
            HttpURLConnection conn = (HttpURLConnection) dist.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("USERNAME" , "");
            conn.setRequestProperty("TOKEN" , "");
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            in.close();
        }
    }

    @SneakyThrows
    public void buildCollection(String db_name,String collection_name , String json){
        for(String url : urls) {
            if(("http://w" +Affinity.getInstance().getValue() + ":8080").equalsIgnoreCase(url))
                continue;
            URL dist = new URL(url + "/worker/db/add/collection/" + db_name + "/" + collection_name +"/noupdate/false");
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
        }
    }
    @SneakyThrows
    public boolean checkWorkersCollection(String db_name , String collection_name){
        for(String url : urls) {
            if(("http://w" +Affinity.getInstance().getValue() + ":8080").equalsIgnoreCase(url))
                continue;
            URL dist = new URL(url + "/worker/db/find/" + db_name + "/" + collection_name);
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
            if(response.toString().equals("true"))
                return true;
        }
        return false;
    }

    @SneakyThrows
    public boolean checkIdDocument(String db_name , String collection_name , String json){
        for(String url : urls) {
            if(("http://w" +Affinity.getInstance().getValue() + ":8080").equalsIgnoreCase(url))
                continue;
            URL dist = new URL(url + "/worker/db/index/" + db_name + "/" + collection_name);
            HttpURLConnection conn = (HttpURLConnection) dist.openConnection();
            conn.setRequestMethod("GET");
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
            if(response.toString().equals("true"))
                return true;
        }
        return false;
    }

    @SneakyThrows
    public void buildDocument(String db_name , String collection_name , String json){
        for(String url : urls) {
            if(("http://w" +Affinity.getInstance().getValue() + ":8080").equalsIgnoreCase(url))
                continue;
            URL dist = new URL(url + "/worker/db/add/document/" + db_name + "/" + collection_name +"/noupdate/false");
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
        }
    }

    @SneakyThrows
    public void deleteCollection(String db_name , String collection_name){
        for(String url : urls) {
            if(("http://w" +Affinity.getInstance().getValue() + ":8080").equalsIgnoreCase(url))
                continue;
            URL dist = new URL(url + "/worker/db/delete/collection/" + db_name + "/" + collection_name +"/noupdate/false");
            HttpURLConnection conn = (HttpURLConnection) dist.openConnection();
            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("USERNAME" , "");
            conn.setRequestProperty("TOKEN" , "");
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            in.close();
        }
    }

    @SneakyThrows
    public void deleteDatabase(String db_name){
        for(String url : urls) {
            if(("http://w" +Affinity.getInstance().getValue() + ":8080").equalsIgnoreCase(url))
                continue;
            URL dist = new URL(url + "/worker/db/delete/database/" + db_name + "/noupdate/false");
            HttpURLConnection conn = (HttpURLConnection) dist.openConnection();
            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("USERNAME" , "");
            conn.setRequestProperty("TOKEN" , "");
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            in.close();
        }
    }

    @SneakyThrows
    public void updateDocument(String db_name , String collection_name ,
                               String id , String prop , String value){
        for(String url : urls) {
            if(("http://w" +Affinity.getInstance().getValue() + ":8080").equalsIgnoreCase(url))
                continue;
            URL dist = new URL(url + "/worker/db/update/" + db_name + "/" + collection_name +"/"+id+"/"+prop+"/noupdate/false");
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
        }
    }

    @SneakyThrows
    public void deleteDocument(String db_name , String collection_name , String value){
        for(String url : urls) {
            if(("http://w" +Affinity.getInstance().getValue() + ":8080").equalsIgnoreCase(url))
                continue;
            URL dist = new URL(url + "/worker/db/delete/document/" + db_name + "/" +collection_name + "/" + value + "/noupdate/false");
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
        }
    }


}
