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
public class AntController {

    @Resource
    private BaseDataInit baseDataInit;

    @RequestMapping("/ant")
    public String ant(Locale locale, Model model) {
        return "ant";
    }

    @RequestMapping(value = "/data")
    public @ResponseBody Page<RoadBo> data(int pageNum, int pageSize) {
        Page<RoadBo> page = new Page<>();
        List<RoadBo> roadBoList = baseDataInit.getRoadBoList();
        page.setTotal(roadBoList.size());
        page.setPageSize(pageSize);
        page.setCurrentPage(pageNum);
        page.setContent(page(roadBoList, pageNum, pageSize));
        return page;
    }

    private List<RoadBo> page(List<RoadBo> list, int pageNum, int pageSize) {
        if (list == null) {
            return null;
        }
        if (list.size() == 0) {
            return null;
        }

        Integer count = list.size(); // 记录总数
        Integer pageCount = 0; // 页数
        if (count % pageSize == 0) {
            pageCount = count / pageSize;
        } else {
            pageCount = count / pageSize + 1;
        }

        int fromIndex = 0; // 开始索引
        int toIndex = 0; // 结束索引

        if (pageNum != pageCount) {
            fromIndex = (pageNum - 1) * pageSize;
            toIndex = fromIndex + pageSize;
        } else {
            fromIndex = (pageNum - 1) * pageSize;
            toIndex = count;
        }

        return list.subList(fromIndex, toIndex);

    }
}