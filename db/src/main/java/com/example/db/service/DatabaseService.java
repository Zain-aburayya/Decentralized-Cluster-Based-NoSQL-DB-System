package com.example.db.service;

import com.example.db.model.Database;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DatabaseService {
    private final Database database = Database.getInstance();
    @SneakyThrows
    public String createDatabase(String db_name, String update){
        return database.createDB(db_name,update).get();
    }
    @SneakyThrows
    public boolean deleteDatabase(String db_name, String update){
        return database.deleteDB(db_name,update).get();
    }
    public boolean isExist(String db_name){
        return database.isExist(db_name);
    }

    public List<String> getList(){return database.DatabaseList();}

}
