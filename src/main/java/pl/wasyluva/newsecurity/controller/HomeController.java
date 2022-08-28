package pl.wasyluva.newsecurity.controller;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/")
public class HomeController {

    @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
    public String getPrincipal(Authentication authentication){
        Object principal = authentication.getPrincipal();

        List<String> lines = new LinkedList<>();
        if (principal instanceof User) lines.add("Obiekt Principal jest instancją klasy User, która rozszerza interfejs UserDetails.");
        if (authentication instanceof  UsernamePasswordAuthenticationToken) lines.add("Obiekt Authentication po pozytywnym, domyślnym uwierzytelnieniu jest instancją klasy UsernamePasswordAuthenticationToken.");

        StringBuilder htmlBuilder = new StringBuilder();
        for (String line: lines) {
            htmlBuilder.append("<p>" + line + "</p>");
        }

        return htmlBuilder.toString();
    }

    @GetMapping("/test")
    public String doGetTest(){
        return "test";
    }

    @GetMapping("/test/test")
    public String doGetTestTest(){
        return "test test";
    }

    @GetMapping("/test2")
    public String doGetTest2(){
        return "test2";
    }

    @GetMapping("/test2/test")
    public String doGetTest2Test(){
        return "test2 test";
    }
}
