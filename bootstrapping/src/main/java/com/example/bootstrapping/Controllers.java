package com.example.bootstrapping;

import com.example.bootstrapping.token.Base64Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bootstrapping/")
public class Controllers {
    @Autowired
    private Services services;
    @GetMapping("/add/user")
    public String addUser(@RequestHeader("USERNAME") String username){
        if(services.isExistUser(username))
            return "the username already used";
        Base64Token base64Token = new Base64Token();
        String token = base64Token.generateNewToken();
        services.setWorkerUser(username,token);
        services.sendToWorkerUser(username , token);
        services.sendToWeb(username);
        return token;
    }

    @GetMapping("/add/admin")
    public String addAdmin(@RequestHeader("USERNAME") String username){
        if(services.isExistAdmin(username))
            return "the admin already used";
        Base64Token base64Token = new Base64Token();
        String token = base64Token.generateNewToken();
        services.setWorkerAdmin(username,token);
        services.sendToWorkerAdmin(username , token);
        return token;
    }
}
