package com.example.web.control;

import com.example.web.model.User;
import com.example.web.service.CollectionService;
import com.example.web.service.DatabaseService;
import com.example.web.service.DocumentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/document")
public class DocumentController {
    @Autowired
    private CollectionService collectionService;

    @Autowired
    private DatabaseService databaseService;
    @Autowired
    private DocumentService documentService;
    @Autowired
    private HttpSession session;

    @GetMapping("/list")
    public String getList(){
        return "document-list";
    }
    @PostMapping("/list")
    public String postList(@RequestParam("db_name") String db_name,
                           @RequestParam("collection_name") String collection_name,
                           HttpSession session , Model model){
        User login = (User) session.getAttribute("login");
        if(login == null)
            return "login";
        String list = documentService.getList(db_name,collection_name,session);
        model.addAttribute("list",list);
        return "list";
    }

    @GetMapping("/add")
    public String addDocument(){
        User login = (User) session.getAttribute("login");
        if(login == null)
            return "login";
        return "document";
    }

    @PostMapping("/add")
    public String postAddDocument(@RequestParam("db_name") String db_name,
                                  @RequestParam("collection_name") String collection_name,
                                  HttpSession session,Model model){
        User login = (User) session.getAttribute("login");
        if(login == null)
            return "login";
        String path = "/document/add/" + db_name + "/" + collection_name;
        model.addAttribute("path",path);
        model.addAttribute("propertyList",documentService.schemaProp(db_name,collection_name,session));
        return "add-document";
    }
    @PostMapping("/add/{db_name}/{collection_name}")
    public String postAdd(@PathVariable("db_name") String db_name,
                         @PathVariable("collection_name") String collection_name,
                         @RequestParam Map<String,String> prop,
                         HttpSession session , Model model){
        User login = (User) session.getAttribute("login");
        if(login == null)
            return "login";
        model.addAttribute("result",documentService.addDocument(db_name,collection_name,prop,session));
        return "response";
    }

    @GetMapping("/delete")
    public String getDelete(){
        User login = (User) session.getAttribute("login");
        if(login == null)
            return "login";
        return "document-delete";
    }

    @PostMapping("/delete")
    public String postDelete(@RequestParam ("db_name") String db_name,
                             @RequestParam ("collection_name") String collection_name,
                             @RequestParam("id") String id,
                             HttpSession session , Model model){
        User login = (User) session.getAttribute("login");
        if(login == null)
            return "login";
        String response = documentService.deleteDocument(db_name,collection_name, id ,session);
        model.addAttribute("result",response);
        return "response";
    }

    @GetMapping("/update")
    public String getUpdate(){
        User login = (User) session.getAttribute("login");
        if(login == null)
            return "login";
        return "document-update";
    }

    @PostMapping("/update")
    public String postUpdate(@RequestParam ("db_name") String db_name,
                             @RequestParam ("collection_name") String collection_name,
                             @RequestParam("id") String id,
                             @RequestParam("prop") String prop ,
                             @RequestParam("value") String value,
                             HttpSession session , Model model){
        User login = (User) session.getAttribute("login");
        if(login == null)
            return "login";
        String response = documentService.updateDocument(db_name, collection_name, id, prop, value, session);
        model.addAttribute("result",response);
        return "response";
    }

}
