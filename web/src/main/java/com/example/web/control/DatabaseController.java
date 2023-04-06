package com.example.web.control;

import com.example.web.model.User;
import com.example.web.service.DatabaseService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/database")
public class DatabaseController {
    @Autowired
    private DatabaseService databaseService;

    @Autowired
    private HttpSession session;
    @GetMapping("/create")
    public String getCreateDB(){
        User login = (User) session.getAttribute("login");
        if(login == null)
            return "login";
        return "create-db";
    }
    @PostMapping("/create")
    public String postCreateDB(@RequestParam("db_name") String db_name ,
                               HttpSession session,
                               Model model){
        User login = (User) session.getAttribute("login");
        if(login == null)
            return "login";
        String response = databaseService.createDatabase(db_name,session);
        System.out.println(response);
        if(response.equalsIgnoreCase("database"))
            model.addAttribute("result","add database done");
        else
            model.addAttribute("result",response);
        return "response";
    }

    @GetMapping("/delete")
    public String getDeleteDB(){

        User login = (User) session.getAttribute("login");
        if(login == null)
            return "login";
        return "delete-db";
    }
    @PostMapping("/delete")
    public String postDeleteDB(@RequestParam("db_name") String db_name,
                               HttpSession session,
                               Model model){
        User login = (User) session.getAttribute("login");
        if(login == null)
            return "login";
        if(!databaseService.isExist(db_name,session)){
            model.addAttribute("result","no database");
            return "response";
        }
        databaseService.deleteDatabase(db_name,session);
        model.addAttribute("result" , "deleted done");
        return "response";
    }

    @GetMapping("/list")
    public String getList(HttpSession session, Model model){
        User login = (User) session.getAttribute("login");
        if(login == null)
            return "login";
        String list = databaseService.getList(session);
        model.addAttribute("list",list);
        return "list";
    }
}
