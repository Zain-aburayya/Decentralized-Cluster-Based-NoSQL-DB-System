package com.example.db.control;

import com.example.db.affinity.Affinity;
import com.example.db.authentication.AuthenticationModel;
import com.example.db.authentication.AuthenticationService;
import com.example.db.cluster.Workers;
import com.example.db.service.DocumentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/worker")
public class PutController {

    private final DocumentService documentService;
    private final AuthenticationService authenticationService;
    public PutController(DocumentService documentService ,
                         AuthenticationService authenticationService){
        this.documentService = documentService;
        this.authenticationService = authenticationService;
    }
    @PutMapping("/db/update/{db_name}/{collection_name}/{id}/{prop}/{update}/{affinity}") // done
    @ResponseBody
    public String updateDocument(@PathVariable("db_name") String db_name,
                                 @PathVariable("collection_name") String collection_name,
                                 @PathVariable("id") String id,
                                 @PathVariable("prop") String prop,
                                 @RequestBody String value,
                                 @PathVariable("update") String update ,
                                 @PathVariable("affinity") String affinity,
                                 @RequestHeader("USERNAME") String username,
                                 @RequestHeader("TOKEN") String token){
        if(affinity.equalsIgnoreCase("true")){
            if(!authenticationService.authenticateAdmin(username,token))
                return "no permission";
            Affinity.getInstance().updateDocumentAffinity(db_name, collection_name, id, prop , value);
        }
        else
            return documentService.updateDocument(db_name, collection_name, id, prop , value , update);
        return "update document";
    }

    @GetMapping("/document/list/{db_name}/{collection_name}")
    public String documentList(@PathVariable("db_name") String db_name,
                                                  @PathVariable("collection_name") String collection_name){
        return documentService.getList(db_name,collection_name);
    }

    @GetMapping("/document/schema/{db_name}/{collection_name}")
    public String getSchema(@PathVariable("db_name") String db_name ,
                            @PathVariable("collection_name") String collection_name){
        return documentService.getSchema(db_name, collection_name);
    }
}
