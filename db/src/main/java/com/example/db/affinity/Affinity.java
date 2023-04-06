package com.example.db.affinity;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.SneakyThrows;

import java.io.File;

public class Affinity {
    private final AffinityBroadcast broadcast = new AffinityBroadcast();

    private Affinity(){}
    private static Affinity instance = null;
    public static Affinity getInstance(){
        if(instance == null)
            instance = new Affinity();
        return instance;
    }
    @SneakyThrows
    public void updateAffinity(){
        File file = new File("./src/main/resources/aff.json");
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(file);
        ((ObjectNode) root).put("affinity", (root.get("affinity").intValue() + 1) % 4);
        mapper.writeValue(file, root);
    }

    @SneakyThrows
    public void resetAffinity(){
        File file = new File("./src/main/resources/aff.json");
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(file);
        ((ObjectNode) root).put("affinity", 0);
        mapper.writeValue(file, root);
    }

    public void databaseAffinity(String db_name){
        String port = "w" + getValue();
        broadcast.buildDatabase(db_name,port);
    }

    public void collectionAffinity(String db_name , String collection_name , String json){
        String port = "w" + getValue();
        broadcast.buildCollection(db_name,collection_name,json,port);
    }

    public void documentAffinity(String db_name , String collection_name ,
                                 String json){
        String port = "w" + getValue();
        broadcast.buildDocument(db_name,collection_name,json,port);
    }
    public void deleteDatabaseAffinity(String db_name){
        String port = "w" + getValue();
        broadcast.deleteDatabase(db_name,port);
    }
    public void deleteCollectionAffinity(String db_name, String collection_name){
        String port = "w" + getValue();
        broadcast.deleteCollection(db_name,collection_name,port);
    }

    public void deleteDocumentAffinity(String db_name, String collection_name, String value){
        String port = "w" + getValue();
        broadcast.deleteDocument(db_name,collection_name,value,port);
    }
    public void updateDocumentAffinity(String db_name, String collection_name, String id, String prop, String value){
        String port = "w" + getValue();
        broadcast.updateDocument(db_name,collection_name,id,prop,value,port);
    }

    @SneakyThrows
    public int getValue(){
        File file = new File("./src/main/resources/aff.json");
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(file);
        return root.get("affinity").intValue();
    }
}
