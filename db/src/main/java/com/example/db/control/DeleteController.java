package com.example.db.control;

import com.example.db.affinity.Affinity;
import com.example.db.authentication.AuthenticationService;
import com.example.db.service.CollectionService;
import com.example.db.service.DatabaseService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/worker")
public class DeleteController {
    private final DatabaseService databaseService;
    private final CollectionService collectionService;
    private final AuthenticationService authenticationService;
    public DeleteController(DatabaseService databaseService ,
                            CollectionService collectionService,
                            AuthenticationService authenticationService){
        this.databaseService = databaseService;
        this.collectionService = collectionService;
        this.authenticationService = authenticationService;
    }
    @DeleteMapping("/db/delete/database/{db_name}/{update}/{affinity}") // done
    public String deleteDatabase(@PathVariable("db_name") String db_name,
                                 @PathVariable("update") String update,
                                 @PathVariable("affinity") String affinity,
                                 @RequestHeader("USERNAME") String username,
                                 @RequestHeader("TOKEN") String token){
        if(affinity.equalsIgnoreCase("true")){
            if(!authenticationService.authenticateAdmin(username,token))
                return "no permission";
            return Affinity.getInstance().deleteDatabaseAffinity(db_name);
        }
        return databaseService.deleteDatabase(db_name,update) ? "delete-success" : "delete-fail";
    }

    @DeleteMapping("/db/delete/collection/{db_name}/{collection_name}/{update}/{affinity}") //done
    public String deleteCollection(@PathVariable("db_name") String db_name,
                                   @PathVariable("collection_name") String collection_name,
                                   @PathVariable("update") String update,
                                   @PathVariable("affinity") String affinity,
                                   @RequestHeader("USERNAME") String username,
                                   @RequestHeader("TOKEN") String token){
        if(affinity.equalsIgnoreCase("true")){
            if(!authenticationService.authenticateAdmin(username,token))
                return "no permission";
            return Affinity.getInstance().deleteCollectionAffinity(db_name,collection_name);
        }
        return collectionService.deleteCollection(db_name,collection_name,update) ? "delete-success" : "delete-fail";
    }

}
