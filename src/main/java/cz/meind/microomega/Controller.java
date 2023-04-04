package cz.meind.microomega;

import cz.meind.microomega.Database.Database;
import cz.meind.microomega.Service.Login;
import cz.meind.microomega.Service.Register;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@org.springframework.stereotype.Controller
public class Controller {
    @ResponseBody
    @GetMapping(value = "/login", produces = "text/html")
    public String login() {
        return Database.read("login.html");
    }

    @ResponseBody
    @PostMapping(value = "/login", produces = "text/html")
    public String loginPost(@RequestBody String requestBody) {
        if (Login.login(requestBody))
            return "<script>alert(\"Logged in!\")</script>";
        return login() + "<script>alert(\"Wrong password!\")</script>";
    }

    @ResponseBody
    @GetMapping(value = "/register", produces = "text/html")
    public String register() {
        return Database.read("register.html");
    }

    @ResponseBody
    @PostMapping(value = "/register", produces = "text/html")
    public String registerPost(@RequestBody String requestBody) {
        if (Register.register(requestBody))
            return login();
        return register() + "<script>alert(\"Username already exists!\")</script>";
    }
}
