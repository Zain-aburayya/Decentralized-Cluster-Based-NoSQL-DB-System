package com.example.db.model;


import com.example.db.affinity.Affinity;
import com.example.db.cluster.Workers;
import com.example.db.index.HashIndexing;
import com.example.db.json.JsonValidation;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.SneakyThrows;

import java.io.*;

public class Document {

    private final JsonValidation jsonValidation = new JsonValidation();
    private final Workers workers = new Workers();
    @SneakyThrows
    public String addDocument(String db_name , String collection_name , String json, String update){
        String path = Database.getInstance().getDB_PATH() + db_name + "/" + collection_name + ".json";
        File file = new File(path);
        boolean isUpdate = update.equalsIgnoreCase("update");
        if(!file.exists()){
            return "file doesn't exist ...";
        }
        if(!jsonValidation.schemaValidator(db_name , collection_name ,json)){
            return "failed to add document...";
        }
        if(HashIndexing.getInstance().isExistId(db_name,collection_name,json)){
           return "Id is already exist ...";
        }

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode root = (ObjectNode) objectMapper.readTree(new File(path));
        ArrayNode data = (ArrayNode) root.get("data");
        if (data == null) {
            data = objectMapper.createArrayNode();
            root.set("data", data);
        }
        JsonNode jsonNode = objectMapper.readTree(json);
        addToIndexing(db_name, collection_name, json);
        data.add(jsonNode);
        FileWriter writer = new FileWriter(path);
        objectMapper.writeValue(writer, root);
        writer.close();
        if (isUpdate)
            workers.buildDocument(db_name,collection_name,json);
        Affinity.getInstance().updateAffinity();
        return "success add a document ... ";
    }

    @SneakyThrows
    public void addToIndexing(String db_name , String collection_name , String json){
        HashIndexing
                .getInstance()
                .addToIndexing(db_name, collection_name, json);
    }

    public String getByProperty(String db_name , String collection_name , String prop , String value){
        return HashIndexing
                .getInstance()
                .getByProperty(db_name, collection_name, prop, value);
    }

    public String getById(String db_name , String collection_name ,String value){
        return HashIndexing
                .getInstance()
                .getByProperty(db_name, collection_name, "id", value);
    }

    public String deleteById(String db_name , String collection_name ,
                           String value, String update){
        return HashIndexing.getInstance().deleteById(db_name, collection_name, value , update);
    }

    public String updateDocument(String db_name , String collection_name ,
                                 String id , String prop , String value , String update){
        return HashIndexing.getInstance().updateById(db_name, collection_name, id, prop ,value , update);
    }

    @SneakyThrows
    public String getList(String dbName, String collectionName) {
        String path = Database.getInstance().getDB_PATH() + dbName + "/" + collectionName + ".json";
        File file = new File(path);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(file);
        JsonNode authNode = root.get("data");
        return authNode.toString();
    }

    @SneakyThrows
    public String getSchema(String db_name , String collection_name){
        String path = Database.getInstance().getDB_PATH() + db_name +"/schema/schema_" + collection_name + ".json";
        File file = new File(path);
        FileInputStream fis = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        String line;
        StringBuilder res = new StringBuilder();
        while ((line = br.readLine()) != null) {
            res.append(line);
        }
        br.close();
        return res.toString();
    }
}
