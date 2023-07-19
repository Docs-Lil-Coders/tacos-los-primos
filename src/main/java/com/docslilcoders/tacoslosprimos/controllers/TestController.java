package com.docslilcoders.tacoslosprimos.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {
//PLEASE DO NOT DELETE ANYONE ELSE'S TEST METHODS. JUST ADD A NEW ONE WITH A BS URL TO TEST YOUR TEST FILES :)


    //please don't delete this one, it's just an example for copy/paste and then edit the pasted one to fit
    @GetMapping("/yourTestUrl")
    public String exampleMethodName() {
        return "yourTestFile";
    }
}