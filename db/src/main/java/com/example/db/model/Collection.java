package com.example.db.model;
import com.example.db.affinity.Affinity;
import com.example.db.cluster.Workers;
import com.example.db.lock.Lock;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.SneakyThrows;
import lombok.Synchronized;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Collection {

    private final Workers workers = new Workers();

    public  boolean isExist(String db_name , String collection_name){
        String path = Database.getInstance().getDB_PATH() + db_name+ "/" + collection_name + ".json";
        File file = new File(path);
        return file.exists();
    }
    @SneakyThrows
    public String addCollection(String db_name, String collection_name , String json, String update) {
        String path = Database.getInstance().getDB_PATH() + db_name;
        if(!Database.getInstance().isExist(db_name)){
            return "database not found ...";
        }
        String jsonFile = path + "/" + collection_name + ".json";
        boolean isUpdate = update.equalsIgnoreCase("update");
        Object lock = Lock.getInstance().getLock(db_name + "-" + collection_name);
        synchronized(lock){
            if(isUpdate && this.isExist(db_name,collection_name)){
                if(workers.checkWorkersCollection(db_name,collection_name)){
                    return "database is already exist ...";
                }
            }
            else if(this.isExist(db_name,collection_name)){
                return "database is already exist ...";
            }
            if (isUpdate)
                workers.buildCollection(db_name, collection_name, json);
            JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("data", jsonArray);
            path += "/schema/schema_" + collection_name + ".json";
            addSchema(path, json);
            FileWriter fileWriter = new FileWriter(jsonFile);
            fileWriter.write(jsonObject.toString());
            fileWriter.close();
            Affinity.getInstance().updateAffinity();
        }
        return "added Collection ...";
    }
    @SneakyThrows
    public void addSchema(String path , String schema ){
        ObjectMapper objectMapper = new ObjectMapper();

        ObjectNode schemaNode = objectMapper.createObjectNode();
        schemaNode.put("$schema", "http://json-schema.org/draft-04/schema#");
        schemaNode.put("type", "object");

        ObjectNode propertiesNode = objectMapper.createObjectNode();
        JsonNode schemaJsonNode = objectMapper.readTree(schema);
        schemaJsonNode.fields().forEachRemaining(entry -> {
            propertiesNode.set(entry.getKey(), objectMapper.createObjectNode()
                    .put("type", "string"));
        });
        schemaNode.set("properties", propertiesNode);

        ArrayNode requiredNode = objectMapper.createArrayNode();
        schemaJsonNode.fields().forEachRemaining(entry -> {
            requiredNode.add(entry.getKey());
        });
        schemaNode.set("required", requiredNode);
        schemaNode.put("additionalProperties", false);
        File file = new File(path);
        objectMapper.writeValue(file, schemaNode);
    }
    public Boolean deleteCollection(String db_name, String collection_name, String update){
        Object lock = Lock.getInstance().getLock(db_name + "-" + collection_name);
        String path = Database.getInstance().getDB_PATH() + db_name + "/" + collection_name + ".json";
        File file = new File(path);
        synchronized (lock){
            if (file.exists()) {
                if (update.equalsIgnoreCase("update"))
                    workers.deleteCollection(db_name, collection_name);
                Affinity.getInstance().updateAffinity();
                file.delete();
                path = Database.getInstance().getDB_PATH() + db_name + "/schema/schema_" + collection_name + ".json";
                file = new File(path);
                return file.delete();
            }
        }
        return false;
    }

    public List<String> getList(String db_name) {
        String path = "./src/main/resources/Database/" + db_name;
        File file = new File(path);
        if(!file.exists())
            return new ArrayList<>();
        List<String> fileList = Arrays.asList(file.list());
        List<String> tempList = new ArrayList<>();
        for(int i = 1 ; i < fileList.size() ; i++){
            tempList.add(fileList.get(i));
        }
        return tempList;
    }
}
