package com.stockia.stockia.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/") 
public class UserController {

    @GetMapping("/hola")
    public String TestController(){
        return "Hola Mundo";
    }
}
