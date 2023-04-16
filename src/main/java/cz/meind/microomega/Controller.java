package cz.meind.microomega;

import cz.meind.microomega.Database.Database;
import cz.meind.microomega.Service.Login;
import cz.meind.microomega.Service.Register;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import java.io.IOException;

@org.springframework.stereotype.Controller
public class Controller {

    @ResponseBody
    @GetMapping(value = "/", produces = "text/html")
    public ResponseEntity<String> index(HttpServletResponse response, HttpServletRequest req) throws IOException {
        if (Login.checkCookie(req.getCookies()[0].getValue())) return new ResponseEntity<>("Logged", HttpStatus.OK);
        response.sendRedirect("/login");
        return new ResponseEntity<>(HttpStatus.PERMANENT_REDIRECT);
    }

    @ResponseBody
    @GetMapping(value = "/login", produces = "text/html")
    public ResponseEntity<String> login(HttpServletResponse response, HttpServletRequest req) throws IOException {
        if (Login.checkCookie(req.getCookies()[0].getValue())) {
            response.sendRedirect("/");
            return new ResponseEntity<>(HttpStatus.PERMANENT_REDIRECT);
        }
        return new ResponseEntity<>(Database.read("login.html"), HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping(value = "/login", produces = "text/html")
    public ResponseEntity<String> loginPost(@RequestBody String requestBody, HttpServletResponse response, HttpServletRequest req) throws IOException {
        String login = Login.login(requestBody);
        if (login != null) {
            Cookie cookie = new Cookie("id", login);
            cookie.setPath("/");
            cookie.setMaxAge(-1);
            response.addCookie(cookie);
            response.sendRedirect("/");
            return new ResponseEntity<>(HttpStatus.PERMANENT_REDIRECT);
        }
        response.sendRedirect("/login");
        return new ResponseEntity<>(HttpStatus.PERMANENT_REDIRECT);
    }

    @ResponseBody
    @GetMapping(value = "/register", produces = "text/html")
    public ResponseEntity<String> register(HttpServletResponse response, HttpServletRequest req) throws IOException {
        if (Login.checkCookie(req.getCookies()[0].getValue())) {
            response.sendRedirect("/");
            return new ResponseEntity<>(HttpStatus.PERMANENT_REDIRECT);
        }
        return new ResponseEntity<>(Database.read("register.html"), HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping(value = "/register", produces = "text/html")
    public ResponseEntity<String> registerPost(@RequestBody String requestBody, HttpServletRequest req, HttpServletResponse response) throws IOException {
        if (Register.register(requestBody)) {
            response.sendRedirect("/login");
            return new ResponseEntity<>(HttpStatus.PERMANENT_REDIRECT);
        }
        return new ResponseEntity<>("<script>alert(\"Username already exists!\")</script>", HttpStatus.OK);
    }
}
