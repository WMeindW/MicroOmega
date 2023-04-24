package cz.meind.microomega;

import cz.meind.microomega.Database.Database;
import cz.meind.microomega.Service.Login;
import cz.meind.microomega.Service.Register;
import cz.meind.microomega.Service.Service;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@org.springframework.stereotype.Controller
public class Controller {

    @ResponseBody
    @GetMapping(value = "/", produces = "text/html")
    public ResponseEntity<String> index(HttpServletResponse response, HttpServletRequest req) throws IOException {
        if (req.getCookies() != null) {
            if (req.getCookies().length != 0 && Login.checkCookie(req.getCookies()))
                return new ResponseEntity<>("Logged", HttpStatus.OK);
        }
        response.sendRedirect("/login");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping(value = "/login", produces = "text/html")
    public ResponseEntity<String> login(HttpServletResponse response, HttpServletRequest req) throws IOException {
        if (req.getCookies() != null) {
            if (req.getCookies().length != 0 && Login.checkCookie(req.getCookies())) {
                response.sendRedirect("/");
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(Database.read("login.html"), HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping(value = "/login", produces = "text/html")
    public ResponseEntity<String> loginPost(@RequestBody String requestBody, HttpServletResponse response, HttpServletRequest req) throws IOException {
        if (req.getCookies() != null) {
            if (req.getCookies().length != 0 && Login.checkCookie(req.getCookies())) {
                response.sendRedirect("/");
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        String login = Login.login(requestBody);
        if (login != null) {
            Cookie cookie = new Cookie("id", login);
            cookie.setPath("/");
            cookie.setMaxAge(-1);
            response.addCookie(cookie);
            response.sendRedirect("/");
            return new ResponseEntity<>(HttpStatus.OK);
        }
        response.sendRedirect("/login");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping(value = "/register", produces = "text/html")
    public ResponseEntity<String> register(HttpServletResponse response, HttpServletRequest req) throws IOException {
        if (req.getCookies() != null) {
            if (req.getCookies().length != 0 && Login.checkCookie(req.getCookies())) {
                response.sendRedirect("/");
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(Database.read("register.html"), HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping(value = "/register", produces = "text/html")
    public ResponseEntity<String> registerPost(@RequestBody String requestBody, HttpServletRequest req, HttpServletResponse response) throws IOException {
        if (req.getCookies() != null) {
            if (req.getCookies().length != 0 && Login.checkCookie(req.getCookies())) {
                response.sendRedirect("/");
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        if (Register.register(requestBody)) {
            response.sendRedirect("/login");
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>("<script>alert(\"Error!\")</script>", HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping(value = "/sendHook", produces = "text/html")
    public ResponseEntity<String> sendHook(@RequestBody String data, HttpServletRequest req) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Allow-Origin", "*");
        if (req.getCookies() != null) {
            if (req.getCookies().length != 0 && Login.checkCookie(req.getCookies())) {
                return new ResponseEntity<>(Service.sendHook(data), headers, HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(Service.sendHook(data), headers, HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping(value = "/info", produces = "text/html")
    public ResponseEntity<String> info(@RequestBody String data, HttpServletRequest req) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Allow-Origin", "*");
        if (req.getCookies() != null) {
            if (req.getCookies().length != 0 && Login.checkCookie(req.getCookies())) {
                return new ResponseEntity<>(Service.info(data), headers, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(headers, HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @PostMapping(value = "/friends", produces = "text/html")
    public ResponseEntity<String> friends(@RequestBody String data, HttpServletRequest req) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Allow-Origin", "*");
        if (req.getCookies() != null) {
            if (req.getCookies().length != 0 && Login.checkCookie(req.getCookies())) {
                return new ResponseEntity<>(Service.friends(data), headers, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(headers, HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @PostMapping(value = "/query", produces = "text/html")
    public ResponseEntity<String> query(@RequestBody String data, HttpServletRequest req) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Allow-Origin", "*");
        if (req.getCookies() != null) {
            if (req.getCookies().length != 0 && Login.checkCookie(req.getCookies())) {
                return new ResponseEntity<>(Service.query(data), headers, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(headers, HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @PostMapping(value = "/add", produces = "text/html")
    public ResponseEntity<Boolean> add(@RequestBody String data, HttpServletRequest req) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Allow-Origin", "*");
        if (req.getCookies() != null) {
            if (req.getCookies().length != 0 && Login.checkCookie(req.getCookies())) {
                return new ResponseEntity<>(Service.add(data), headers, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(headers, HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @PostMapping(value = "/send", produces = "text/html")
    public ResponseEntity<Boolean> send(@RequestBody String data, HttpServletRequest req) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Allow-Origin", "*");
        if (req.getCookies() != null) {
            if (req.getCookies().length != 0 && Login.checkCookie(req.getCookies())) {
                return new ResponseEntity<>(Service.send(data), headers, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(headers, HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @PostMapping(value = "/message", produces = "text/html")
    public ResponseEntity<String> messages(@RequestBody String data, HttpServletRequest req) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Allow-Origin", "*");
        if (req.getCookies() != null) {
            if (req.getCookies().length != 0 && Login.checkCookie(req.getCookies())) {
                return new ResponseEntity<>(Service.messages(data), headers, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(headers, HttpStatus.BAD_REQUEST);
    }

}
