package com.example.web.control;

import com.example.web.model.User;
import com.example.web.service.AuthenticationService;
import com.example.web.service.EmployeeService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @Autowired
    private AuthenticationService authenticate;
    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private HttpSession session;
    @GetMapping("/")
    public String login(){
        User login = (User) session.getAttribute("login");
        if(login == null)
            return "login";
        return "home";
    }

    @GetMapping("/sign-in")
    public String GetSignIn(){
        User login = (User) session.getAttribute("login");
        if(login != null)
            return "home";
        return "sign-in";
    }

    @PostMapping("/sign-in")
    public String PostSignIn(@RequestParam("username") String username,
                             @RequestParam("token") String token,
                             Model model , HttpSession session){
        if(authenticate.checkAdmin(username,token)){
            User login = new User(username,token);
            session.setAttribute("login",login);
            return "home";
        }
        if(!authenticate.checkUsername(username)){
            model.addAttribute("result" , "you are not signing in");
            return "response";
        }
        if(!authenticate.checkToken(username,token)){
            model.addAttribute("result" , "Invalid token");
            return "response";
        }
        User login = new User(username,token);
        session.setAttribute("login",login);
        return "home";
    }

    @GetMapping("/sign-up")
    public String signUp(){
        User login = (User) session.getAttribute("login");
        if(login != null)
            return "home";
        return "sign-up-page";
    }

    @GetMapping("/sign-up/admin")
    public String signUpAdmin(){
        User login = (User) session.getAttribute("login");
        if(login != null)
            return "home";
        return "sign-up-admin";
    }

    @GetMapping("/sign-up/user")
    public String signUpUser(){
        User login = (User) session.getAttribute("login");
        if(login != null)
            return "home";
        return "sign-up-user";
    }

    @PostMapping("/sign/admin")
    public String signAdmin(@RequestParam("username") String username,
                            @RequestParam("admin") String admin,
                            @RequestParam("token") String token,
                            Model model){
        User login = (User) session.getAttribute("login");
        if(login != null)
            return "home";
        if(!authenticate.checkAdmin(admin, token)){
            model.addAttribute("result" , "Invalid admin or token");
            return "response";
        }
        model.addAttribute("result" , employeeService.getToken(username,"admin"));
        return "response";
    }

    @PostMapping("/sign/user")
    public String signUser(@RequestParam("username") String username,
                            Model model){
        User login = (User) session.getAttribute("login");
        if(login != null)
            return "home";
        model.addAttribute("result" , employeeService.getToken(username,"user"));
        return "response";
    }
}
