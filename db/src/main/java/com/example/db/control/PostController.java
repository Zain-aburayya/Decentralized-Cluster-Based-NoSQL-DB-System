package com.example.db.control;

import com.example.db.affinity.Affinity;
import com.example.db.authentication.AuthenticationService;
import com.example.db.service.CollectionService;
import com.example.db.service.DocumentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/worker")
public class PostController {
    private final CollectionService collectionService;
    private final DocumentService documentService;
    private final AuthenticationService authenticationService;
    public PostController( CollectionService collectionService,
                           DocumentService documentService,
                           AuthenticationService authenticationService){
        this.collectionService = collectionService;
        this.documentService = documentService;
        this.authenticationService = authenticationService;
    }
    @PostMapping("/db/add/collection/{db_name}/{collection_name}/{update}/{affinity}") // done
    @ResponseBody
    public String addCollection(@PathVariable("db_name") String db_name ,
                                @PathVariable("collection_name") String collection_name,
                                @RequestBody String schema, @PathVariable String update,
                                @PathVariable("affinity") String affinity,
                                @RequestHeader("USERNAME") String username,
                                @RequestHeader("TOKEN") String token){
        if(affinity.equalsIgnoreCase("true")){
            if(!authenticationService.authenticateUser(username,token)
                    && !authenticationService.authenticateAdmin(username,token))
                return "no permission";
            return Affinity.getInstance().collectionAffinity(db_name,collection_name,schema);
        }
        return collectionService.addCollection(db_name,collection_name,schema,update);
    }
    @PostMapping("/db/add/document/{db_name}/{collection_name}/{update}/{affinity}") // done
    @ResponseBody
    public String addDocument(@PathVariable("db_name") String db_name,
                              @PathVariable("collection_name") String collection_name,
                              @RequestBody String json, @PathVariable String update,
                              @PathVariable("affinity") String affinity,
                              @RequestHeader("USERNAME") String username,
                              @RequestHeader("TOKEN") String token){
        if(affinity.equalsIgnoreCase("true")){
            if(!authenticationService.authenticateUser(username,token)
                    && !authenticationService.authenticateAdmin(username,token))
                return "no permission";
            return Affinity.getInstance().documentAffinity(db_name,collection_name,json);
        }
        return documentService.addDocument(db_name, collection_name, json,update);
    }
}
