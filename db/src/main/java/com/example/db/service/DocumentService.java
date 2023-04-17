package com.example.db.service;

import com.example.db.authentication.AuthenticationModel;
import com.example.db.model.Document;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentService {
    private final Document document = new Document();
    public String addDocument(String db_name , String collection_name , String json, String update){
        return document.addDocument(db_name,collection_name,json,update);
    }

    public String getByProperty(String db_name , String collection_name ,
                                       String prop , String value){
        return document.getByProperty(db_name, collection_name, prop, value);
    }

    public String deleteById(String db_name , String collection_name ,
                           String value, String update){
        return document.deleteById(db_name, collection_name,value , update);
    }
    public String updateDocument(String db_name , String collection_name ,
                               String id , String prop , String value , String update){
        return document.updateDocument(db_name, collection_name, id, prop , value,update);
    }

    public String getList(String dbName, String collectionName) {
        return document.getList(dbName,collectionName);
    }

    public String getSchema(String db_name , String collection_name){
        return document.getSchema(db_name, collection_name);
    }
}
