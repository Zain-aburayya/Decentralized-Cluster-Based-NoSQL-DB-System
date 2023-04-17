package com.example.db.index;

import com.example.db.affinity.Affinity;
import com.example.db.cluster.Workers;
import com.example.db.model.Database;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.SneakyThrows;

import java.io.File;
import java.io.FileWriter;
import java.util.*;

public class HashIndexing {
    HashMap<Indexing, List<Integer>> hashMap = new HashMap<>();
    private final Workers workers = new Workers();
    private static HashIndexing instance = null;
    public static HashIndexing getInstance(){
        if( instance == null)
            instance = new HashIndexing();
        return instance;
    }

    public String getByProperty(String db_name , String collection_name ,
                                       String prop , String value){
        Indexing indexing = new Indexing(db_name,collection_name,prop,value);
        List<Integer> list = null;
        if(hashMap.containsKey(indexing)){
            list = hashMap.get(indexing);
        }
        if(list == null)
            return "Empty list.";
        return list.toString();
    }

    @SneakyThrows
    public boolean isExistId(String db_name , String collection_name , String json){
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> jsonMap = objectMapper.convertValue(
                objectMapper.readTree(json),
                new TypeReference<>() {
                }
        );
        int index = -1;
        for(Map.Entry<String, String> entry : jsonMap.entrySet()){
            if (entry.getKey().equals("id")){
                index = getIndex(db_name,collection_name,entry.getValue());
                break;
            }
        }
        return index != -1;
    }

    @SneakyThrows
    public void addToIndexing(String db_name , String collection_name ,
                              String json){
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> jsonMap = objectMapper.convertValue(
                objectMapper.readTree(json),
                new TypeReference<>() {
                }
        );
        for(Map.Entry<String, String> entry : jsonMap.entrySet()){
            Indexing indexing = new Indexing(db_name,collection_name,entry.getKey(),entry.getValue());
            List<Integer> list = new ArrayList<>();
            if(hashMap.containsKey(indexing)){
                list = hashMap.get(indexing);
            }
            list.add(size(db_name,collection_name) + 1);
            hashMap.put(indexing,list);
        }
    }

    @SneakyThrows
    public int size(String db_name , String collection_name){
        String jsonFile = Database.getInstance().getDB_PATH() + db_name + "/" + collection_name + ".json";
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode root = (ObjectNode) objectMapper.readTree(new File(jsonFile));
        ArrayNode data = (ArrayNode) root.get("data");
        return data.size();
    }

    public void deleteFromIndexing(String db_name , String collection_name ,
                                   String prop , String value , int index){
        Indexing indexing = new Indexing(db_name,collection_name,prop,value);
        hashMap.get(indexing).remove(getIndexList(hashMap.get(indexing) , index));
    }

    @SneakyThrows
    public String deleteById(String db_name , String collection_name ,
                           String value, String update){
        return deleteAllProperty(db_name,collection_name,value,update);
    }

    private int getIndex(String db_name, String collection_name, String id){
        Indexing indexing = new Indexing(db_name,collection_name,"id",id);
        List<Integer> list = hashMap.get(indexing);
        if(list == null || list.size() != 1)
            return -1;
        return list.get(0) - 1;
    }

    @SneakyThrows
    public String deleteAllProperty(String db_name , String collection_name , String  id, String update){
        int index = getIndex(db_name,collection_name,id);
        String path = Database.getInstance().getDB_PATH() + db_name + "/" + collection_name + ".json";
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode root = (ObjectNode) objectMapper.readTree(new File(path));
        ArrayNode data = (ArrayNode) root.get("data");
        int indexInData = -1;
        for(int i = 0 ; i < data.size() ; i++){
            if(data.get(i).get("id").toString().equals("\""+id+"\"")) {
                indexInData = i;
                break;
            }
        }
        if(index == -1 || indexInData == -1) {
            return "not found this document";
        }
        deleteFromFile(db_name,collection_name,indexInData);
        Map<String, String> jsonMap = objectMapper.convertValue(
                objectMapper.readTree(data.get(indexInData).toString()),
                new TypeReference<>() {
                }
        );
        for(Map.Entry<String, String> entry :jsonMap.entrySet() ){
            deleteFromIndexing(db_name,collection_name, entry.getKey(), entry.getValue() , index + 1);
        }
        if(update.equalsIgnoreCase("update")) {
            workers.deleteDocument(db_name,collection_name,id);
        }
        Affinity.getInstance().updateAffinity();
        return "delete document done ...";
    }

    @SneakyThrows
    private void deleteFromFile(String db_name, String collection_name, int index) {
        String path = Database.getInstance().getDB_PATH() + db_name + "/" + collection_name + ".json";
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode root = (ObjectNode) objectMapper.readTree(new File(path));
        ArrayNode data = (ArrayNode) root.get("data");
        data.remove(index);
        FileWriter writer = new FileWriter(path);
        objectMapper.writeValue(writer, root);
        writer.close();
    }

    @SneakyThrows
    public String updateById(String db_name , String collection_name ,
                             String id , String prop , String value , String update){
        int index = getIndex(db_name,collection_name,id);
        if(prop.equalsIgnoreCase("id"))
            return "you can't update an id ...";
        if(index == -1)
            return "id not found ... ";
        String path = Database.getInstance().getDB_PATH() + db_name + "/" + collection_name + ".json";
        File file = new File(path);
        if(!file.exists())
            return "file not found ...";
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode root = objectMapper.readValue(file, ObjectNode.class);
        ArrayNode data = (ArrayNode) root.get("data");
        if (data == null) {
            return "The array in json file not found ...";
        }
        JsonNode node = data.get(index);
        if(node == null)
            return "JsonNode is null ...";
        Map<String, String> jsonMap = objectMapper.convertValue(
                objectMapper.readTree(data.get(index).toString()),
                new TypeReference<>() {
                }
        );
        String preValue = "";
        for(Map.Entry<String, String> entry :jsonMap.entrySet() ){
            if(prop.equals(entry.getKey())){
                preValue = entry.getValue();
                break;
            }
        }
        ((ObjectNode) node).put(prop, value);
        objectMapper.writeValue(file, root);
        return updateIndexing(db_name,collection_name,id,prop,value,preValue,update);
    }

    private String updateIndexing(String db_name , String collection_name ,
                                  String id , String prop , String value ,
                                  String preValue , String update){
        Indexing preIndexing = new Indexing(db_name,collection_name,prop,preValue);
        if(!hashMap.containsKey(preIndexing)){
            return "not found in indexing ...";
        }
        int objectIndex = getIndex(db_name,collection_name,id) + 1;
        int idx = getIndexList(hashMap.get(preIndexing), objectIndex);
        if(idx == -1)
            return "isn't found in the list..";
        hashMap.get(preIndexing).remove(idx);
        Indexing newIndexing = new Indexing(db_name,collection_name,prop,value);
        List<Integer> list = new ArrayList<>();
        if(hashMap.containsKey(newIndexing))
           list = hashMap.get(newIndexing);
        list.add(objectIndex);
        hashMap.put(newIndexing , list);
        if(update.equalsIgnoreCase("update"))
            workers.updateDocument(db_name, collection_name, id, prop, value);
        Affinity.getInstance().updateAffinity();
        return "update complete ...";
    }

    private int getIndexList(List<Integer> list , int find){
        for(int i = 0 ; i < list.size() ; i++){
            if(find == list.get(i))
                return i;
        }
        return -1;
    }
}
