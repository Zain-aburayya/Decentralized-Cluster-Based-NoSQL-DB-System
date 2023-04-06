package com.example.db.control;

import com.example.db.affinity.Affinity;
import com.example.db.authentication.AuthenticationService;
import com.example.db.cluster.Workers;
import com.example.db.index.HashIndexing;
import com.example.db.service.CollectionService;
import com.example.db.service.DatabaseService;
import com.example.db.service.DocumentService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.List;

@RestController
@RequestMapping("/worker")
public class GetController {
    private final DatabaseService databaseService;
    private final DocumentService documentService;
    private final CollectionService collectionService;
    private final AuthenticationService authenticationService;
    public GetController(DatabaseService databaseService ,
                         DocumentService documentService ,
                         CollectionService collectionService,
                         AuthenticationService authenticationService){
        this.databaseService = databaseService;
        this.documentService = documentService;
        this.collectionService = collectionService;
        this.authenticationService = authenticationService;
    }
    @GetMapping("/db/create/database/{db_name}/{update}/{affinity}") //done
    public String createDatabase(@PathVariable("db_name") String db_name,
                                 @PathVariable("update") String update,
                                 @PathVariable("affinity") String affinity,
                                 @RequestHeader("USERNAME") String username,
                                 @RequestHeader("TOKEN") String token){
        if(affinity.equalsIgnoreCase("true")){
            if(!authenticationService.authenticateUser(username,token))
                return "no permission";
            if(databaseService.isExist(db_name))
                return "already exist";
            Affinity.getInstance().databaseAffinity(db_name);
        }
        else
            return databaseService.createDatabase(db_name,update);
        return "database";
    }

    @GetMapping("/db/find/{db_name}")
    @ResponseBody
    public String isExist(@PathVariable("db_name") String db_name){
        return databaseService.isExist(db_name) ? "true" : "false";
    }

    @GetMapping("/db/find/{db_name}/{collection_name}")
    @ResponseBody
    public boolean isExist(@PathVariable("db_name") String db_name,
                          @PathVariable("collection_name") String collection_name){
        return collectionService.isExist(db_name,collection_name);
    }
    @GetMapping("/db/{db_name}/{collection_name}/{prop}/{value}")
    public String getIndex(@PathVariable("db_name") String db_name,
                           @PathVariable("collection_name") String collection_name,
                           @PathVariable("prop") String prop,
                           @PathVariable("value") String value){
        return documentService.getByProperty(db_name, collection_name, prop, value);
    }

    @GetMapping("/db/index/{db_name}/{collection_name}")
    @ResponseBody
    public boolean getIndexOfId(@PathVariable String db_name,
                                @PathVariable String collection_name,
                                @RequestBody String json){
       return HashIndexing.getInstance().isExistId(db_name,collection_name,json);
    }

    @GetMapping("/db/delete/document/{db_name}/{collection_name}/{value}/{update}/{affinity}")
    public String delete(@PathVariable("db_name") String db_name,
                         @PathVariable("collection_name") String collection_name,
                         @PathVariable("value") String value ,
                         @PathVariable("update") String update,
                         @PathVariable("affinity") String affinity,
                         @RequestHeader("USERNAME") String username,
                         @RequestHeader("TOKEN") String token){
        if(affinity.equalsIgnoreCase("true")){
            if(!authenticationService.authenticateUser(username,token))
                return "no permission";
            Affinity.getInstance().deleteDocumentAffinity(db_name,collection_name,value);
        }
        else
            documentService.deleteById(db_name, collection_name, value, update);
        return "delete it successfully ...";
    }

    @GetMapping("/database/list")
    public List<String> DatabaseList(){
        return databaseService.getList();
    }

    @GetMapping("/collection/list/{db_name}")
    public List<String> CollectionList(@PathVariable("db_name") String db_name){
        return collectionService.getList(db_name);
    }

    @SneakyThrows
    @GetMapping("/reset/affinity")
    public String reset(){
        Affinity.getInstance().resetAffinity();
        return "reset done";
    }
}
