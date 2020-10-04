package com.ijson.blog.controller.view;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.ijson.blog.controller.BaseController;
import com.ijson.blog.util.Pageable;
import com.ijson.mongo.support.model.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * desc:
 * version: 6.7
 * Created by cuiyongxu on 2019/12/10 11:23 PM
 */
@Slf4j
@Controller
public class HomeController extends BaseController {


    @RequestMapping("/")
    public ModelAndView root(HttpServletRequest request) {
        return new ModelAndView("/view/index.html");
    }


    @RequestMapping("/index")
    public ModelAndView index(HttpServletRequest request, Integer index, String keyWord) {
        return new ModelAndView("/view/index.html");
    }


    @RequestMapping("/view/admin")
    public ModelAndView adminIndex(HttpServletRequest request, Integer index, String keyWord) {
        ModelAndView view = new ModelAndView("/view/view-admin.html");

        Page page = new Page();
        if (Objects.isNull(index)) {
            index = 1;
        }
        page.setPageNumber(index);
        addViewModelAndView(request, view);
        return view;
    }


}
