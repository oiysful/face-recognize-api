package com.ian.controller.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class ClientController {

    @GetMapping("/registration")
    public String registration() {

        return "registration.html";
    }

    @GetMapping("/authentication")
    public String authentication() {

        return "authentication.html";
    }
}
