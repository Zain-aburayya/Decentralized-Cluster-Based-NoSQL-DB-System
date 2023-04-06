package com.example.db.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;
    @GetMapping("/add/user")
    public String addUser(@RequestHeader("USERNAME") String username ,
                          @RequestHeader("TOKEN") String token){
        authenticationService.addUser(new AuthenticationModel(username,token));
        return "add user";
    }

    @GetMapping("/add/admin")
    public String addAdmin(@RequestHeader("USERNAME") String username ,
                          @RequestHeader("TOKEN") String token){
        authenticationService.addAdmin(new AuthenticationModel(username,token));
        return "add admin";
    }

    @GetMapping("/check/user")
    public String checkUser(@RequestHeader("USERNAME") String username ,
                          @RequestHeader("TOKEN") String token){
        return authenticationService.authenticateUser(username,token) ? "yes" : "no";
    }

    @GetMapping("/check/admin")
    public String checkAdmin(@RequestHeader("USERNAME") String username ,
                            @RequestHeader("TOKEN") String token){
        return authenticationService.authenticateAdmin(username,token) ? "yes" : "no";
    }

    @GetMapping("/check/admin/account")
    public String checkAdminAccount(@RequestHeader("USERNAME") String username){
        return authenticationService.authenticateAdmin(username) ? "yes" : "no";
    }
}
