package com.power.grid.plan.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Locale;

@Controller
public class PowerController {

    @RequestMapping("/create")
    public String create(Locale locale, Model model) {
        return "create";
    }


    @RequestMapping("/show")
    public String show(Locale locale, Model model) {
        return "show";
    }

    @RequestMapping("/initParam")
    public String initParam(Locale locale, Model model) {
        return "initParams";
    }

    @RequestMapping("/humanIntervention")
    public String humanIntervention(Locale locale, Model model) {
        return "humanIntervention";
    }

    @RequestMapping("/schemeShow")
    public String schemeShow(Locale locale, Model model) {
        return "schemeShow";
    }
}