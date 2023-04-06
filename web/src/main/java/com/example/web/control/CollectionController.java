package com.example.web.control;

import com.example.web.model.User;
import com.example.web.service.CollectionService;
import com.example.web.service.DatabaseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/collection")
public class CollectionController {
    @Autowired
    private CollectionService collectionService;

    @Autowired
    private DatabaseService databaseService;
    @Autowired
    private HttpSession session;
    @GetMapping("/create")
    public String getCreateCollection(){
        User login = (User) session.getAttribute("login");
        if(login == null)
            return "login";
        return "create-collection";
    }
    @SneakyThrows
    @PostMapping("/create")
    public String postCreateCollection(@RequestParam("db_name") String db_name ,
                                       @RequestParam("collection_name") String collection_name,
                                       @RequestParam Map<String,String> prop, HttpSession session,
                                       Model model) {
        User login = (User) session.getAttribute("login");
        if(login == null)
            return "login";
        ObjectMapper mapper = new ObjectMapper();
        Map<String,String> jsonMap = new HashMap<>();
        jsonMap.put("id" , "");
        for (Map.Entry<String, String> entry : prop.entrySet()) {
            if(entry.getKey().equals("db_name") || entry.getKey().equals("collection_name")) {
                continue;
            }
            jsonMap.put(entry.getValue() , "");
        }
        String json = mapper.writeValueAsString(jsonMap);
        collectionService.createCollection(db_name,collection_name,json,session);
        return "create-collection";
    }
    @GetMapping("/delete")
    public String getDeleteCollection(){
        User login = (User) session.getAttribute("login");
        if(login == null)
            return "login";
        return "delete-collection";
    }
    @PostMapping("/delete")
    public String postDeleteCollection(@RequestParam("db_name") String db_name,
                                        @RequestParam("collection_name") String collection_name,
                                       HttpSession session , Model model){
        User login = (User) session.getAttribute("login");
        if(login == null)
            return "login";
        if(!collectionService.isExist(db_name,collection_name,session) ||
                !databaseService.isExist(db_name,session)){
            model.addAttribute("result","no database or collection");
            return "response";
        }
        String response = collectionService.deleteCollection(db_name,collection_name,session);
        if(!response.equals("delete-success")){
            model.addAttribute("result",response);
            return "response";
        }
        return "delete-collection";
    }

    @GetMapping("/list")
    public String getList(){
        User login = (User) session.getAttribute("login");
        if(login == null)
            return "login";
        return "collection-list";
    }
    @PostMapping("/list")
    public String postList(@RequestParam("db_name") String db_name,
                                HttpSession session , Model model){
        User login = (User) session.getAttribute("login");
        if(login == null)
            return "login";
        String list = collectionService.getList(db_name,session);
        model.addAttribute("list",list);
        return "list";
    }
}
