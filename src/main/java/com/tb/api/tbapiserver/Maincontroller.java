package com.tb.api.tbapiserver;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Maincontroller {
    
    @GetMapping("/")
	public String mainpage() {
		return "index";
	}
}
