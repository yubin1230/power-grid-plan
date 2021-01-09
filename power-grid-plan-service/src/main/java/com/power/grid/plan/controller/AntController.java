package com.power.grid.plan.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Locale;

@Controller
public class AntController {
	
//    @RequestMapping("/ant")
//	public String ant(Locale locale, Model model) {
//		return "hello";
//	}

	@RequestMapping("/ant")
	public String ant(Locale locale, Model model) {
		return "ant";
	}
}