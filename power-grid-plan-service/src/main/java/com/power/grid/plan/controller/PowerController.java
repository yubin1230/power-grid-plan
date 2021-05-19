package com.power.grid.plan.controller;

import com.power.grid.plan.Page;
import com.power.grid.plan.dto.bo.RoadBo;
import com.power.grid.plan.util.BaseDataInit;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Locale;

@Controller
public class PowerController {

    @Resource
    private BaseDataInit baseDataInit;

    @RequestMapping("/create")
    public String ant(Locale locale, Model model) {
        return "create";
    }

}