package com.example.db.affinity;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.SneakyThrows;

import java.io.File;

public class Affinity {
    private static final String AFFINITY_FIELD = "affinity";
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
    public int getValue(){
        File file = new File("./src/main/resources/aff.json");
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(file);
        return root.get(AFFINITY_FIELD).intValue();
    }

    @SneakyThrows
    public void resetAffinity(){
        File file = new File("./src/main/resources/aff.json");
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(file);
        ((ObjectNode) root).put(AFFINITY_FIELD, 0);
        mapper.writeValue(file, root);
    }

    public String databaseAffinity(String db_name){
        String port = "w" + getValue();
        return broadcast.buildDatabase(db_name,port);
    }

    public String collectionAffinity(String db_name , String collection_name , String json){
        String port = "w" + getValue();
        return broadcast.buildCollection(db_name,collection_name,json,port);
    }
    public String documentAffinity(String db_name , String collection_name ,
                                 String json){
        String port = "w" + getValue();
        return broadcast.buildDocument(db_name,collection_name,json,port);
    }
    public String deleteDatabaseAffinity(String db_name){
        String port = "w" + getValue();
        return broadcast.deleteDatabase(db_name,port);
    }

    public String deleteCollectionAffinity(String db_name, String collection_name){
        String port = "w" + getValue();
        return broadcast.deleteCollection(db_name,collection_name,port);
    }
    public String deleteDocumentAffinity(String db_name, String collection_name, String value){
        String port = "w" + getValue();
        return broadcast.deleteDocument(db_name,collection_name,value,port);
    }
    public String updateDocumentAffinity(String db_name, String collection_name, String id, String prop, String value){
        String port = "w" + getValue();
        return broadcast.updateDocument(db_name,collection_name,id,prop,value,port);
    }
}
