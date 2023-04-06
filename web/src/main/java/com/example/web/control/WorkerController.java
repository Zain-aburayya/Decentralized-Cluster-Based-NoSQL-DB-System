package com.example.web.control;

import com.example.web.model.User;
import com.example.web.model.UserWorker;
import com.example.web.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@Controller
public class WorkerController {
    @Autowired
    private EmployeeService employeeService;
    @GetMapping("/add/user")
    public String addUser(@RequestHeader("USERNAME") String username,
                        @RequestHeader("WORKER") String worker,
                          Model model){
        employeeService.addUser(new UserWorker(username,worker));
        model.addAttribute("result" , "adding successfully");
        return "response";
    }
}
