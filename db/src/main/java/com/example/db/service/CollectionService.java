package com.example.db.service;

import com.example.db.model.Database;
import com.example.db.model.Collection;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollectionService {
    private final Collection collection = new Collection();
    private final Database database = Database.getInstance();
    @SneakyThrows
    public String addCollection(String db_name , String collection_name , String schema, String update){
        return collection.addCollection(db_name,collection_name,schema,update);
    }

    public boolean isExist(String db_name , String collection_name){
        return collection.isExist(db_name,collection_name);
    }

    @SneakyThrows
    public boolean deleteCollection(String db_name , String collection_name, String update){
        return collection.deleteCollection(db_name , collection_name , update);
    }

    public List<String> getList(String db_name) {
        return collection.getList(db_name);
    }
}
