package com.example.db.model;

import com.example.db.affinity.Affinity;
import com.example.db.cluster.Workers;
import org.springframework.scheduling.annotation.Async;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class Database {
    private final String DB_PATH = "./src/main/resources/Database/";
    private final Workers workers = new Workers();

    public String getDB_PATH() {
        return DB_PATH;
    }
    private static Database instance = null;
    private Database(){}
    public static Database getInstance(){
        if(instance == null)
            instance = new Database();
        return instance;
    }
    public boolean isExist(String db_name){
        File dbDirectory = new File(DB_PATH + db_name);
        System.out.println("in exist method ...");
        return dbDirectory.isDirectory() && dbDirectory.exists();
    }

    @Async
    public Future<String> createDB(String db_name, String update){
        if(isExist(db_name)){
            return CompletableFuture.completedFuture("Database is already exist ... ");
        }
        boolean isUpdate = update.equalsIgnoreCase("update");
        if(isUpdate && this.isExist(db_name)){
            if(workers.checkWorkersDatabase(db_name)){
                return CompletableFuture.completedFuture("database is already exist ...");
            }
        }
        else if(this.isExist(db_name)){
            return CompletableFuture.completedFuture("database is already exist ...");
        }
        File schema = new File(DB_PATH + db_name + "/schema");
        schema.mkdirs();
        if(isUpdate)
            workers.buildDatabase(db_name);
        Affinity.getInstance().updateAffinity();
        return CompletableFuture.completedFuture("Creating successfully");
    }
    @Async
    public Future<Boolean> deleteDB(String db_name, String update){
        File file = new File(DB_PATH + db_name);
        if(file.exists() && file.isDirectory()){
            File[] files = file.listFiles();
            if(files != null){
                for(File f : files)
                    f.delete();
            }
            if(update.equalsIgnoreCase("update"))
                workers.deleteDatabase(db_name);
            Affinity.getInstance().updateAffinity();
            return CompletableFuture.completedFuture(file.delete());
        }
        return CompletableFuture.completedFuture(false);
    }

    public List<String> DatabaseList(){
        String path = "./src/main/resources/Database/";
        File file = new File(path);
        if(!file.exists())
            return new ArrayList<>();
        return Arrays.asList(file.list());
    }

}
