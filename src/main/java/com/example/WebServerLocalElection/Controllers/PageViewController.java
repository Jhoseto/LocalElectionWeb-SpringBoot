package com.example.WebServerLocalElection.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageViewController {

    @GetMapping("/")
    public String indexPage() {
        return "index";
    }

    @GetMapping("/verificationPage")
    public String verPage() {
        return "verificationPage";
    }

    @GetMapping("/votePage")
    public String votePage() {
        return "votePage";
    }

    @GetMapping("/info")
    public String infoPage() {
        return "info";
    }
}
