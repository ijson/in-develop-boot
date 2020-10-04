package com.ijson.blog.controller.admin;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ijson.blog.controller.BaseController;
import com.ijson.blog.dao.entity.*;
import com.ijson.blog.model.*;
import com.ijson.blog.service.model.info.*;
import com.ijson.blog.util.EhcacheUtil;
import com.ijson.blog.util.PassportHelper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * https://www.bootcdn.cn/
 * desc: 用户控制台
 * version: 6.7
 * Created by cuiyongxu on 2019/7/27 12:28 AM
 */
@Controller
@RequestMapping("/admin")
public class ConsoleAction extends BaseController {

    @RequestMapping("/console/page")
    public ModelAndView indexv2(HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        view.setViewName("admin/index.html");
        addAdminModelAndView(view, request);
        view.addObject("menu", getMenu(request));
        return view;
    }

    @RequestMapping("/welcome/page")
    public ModelAndView welcome(HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        view.setViewName("admin/welcome.html");
        addAdminModelAndView(view, request);
        view.addObject("systemInfo", SystemInfo.getSystemInfo());
        view.addObject("consoleData", new WelcomeInfo());
        return view;
    }

    @RequestMapping("/default/welcome/page")
    public ModelAndView defaultWelcome(HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        view.setViewName("admin/welcome-default.html");
        view.addObject("consoleData", new WelcomeInfo());
        return view;
    }


    @RequestMapping("/save/user")
    public ModelAndView skipUserAdd(HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        view.setViewName("admin/save-user.html");
        addAdminModelAndView(view, request);
        List<RoleEntity> roleAll = roleService.findAll();
        if (CollectionUtils.isEmpty(roleAll)) {
            view.addObject("roles", Lists.newArrayList());
        } else {
            view.addObject("roles", roleAll.stream().map(RoleInfo::create).collect(Collectors.toList()));
        }
        view.addObject("editData", null);
        return view;
    }




    @RequestMapping("/save/site")
    public ModelAndView siteSettings(HttpServletRequest request) {
        ModelAndView view = new ModelAndView("admin/save-site.html");

        String cookieValue = PassportHelper.getInstance().getCurrCookie(request);
        AuthContext context = (AuthContext) EhcacheUtil.getInstance().get(Constant.loginUserCacheKey, cookieValue);
        if (Objects.isNull(context)) {
            return new ModelAndView(new RedirectView(webCtx));
        }

        List<RoleEntity> roleAll = roleService.findAll();
        if (CollectionUtils.isEmpty(roleAll)) {
            view.addObject("roles", Lists.newArrayList());
        } else {
            view.addObject("roles", roleAll.stream().map(RoleInfo::create).collect(Collectors.toList()));
        }
        view.addObject("site", getConfig());

        addAdminModelAndView(view, request);
        return view;
    }


    @RequestMapping("/save/auth")
    public ModelAndView skipAuthAdd(HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        view.setViewName("admin/save-auth.html");
        addAdminModelAndView(view, request);
        List<AuthEntity> fathers = authService.findFathers("0");
        view.addObject("editData", null);
        view.addObject("fathers", AuthInfo.createAuthList(fathers));

        return view;
    }

    @RequestMapping("/save/role")
    public ModelAndView skipRoleAdd(HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        view.setViewName("admin/save-role.html");
        addAdminModelAndView(view, request);
        List<AuthEntity> allAuth = authService.findAll();
        if (CollectionUtils.isEmpty(allAuth)) {
            view.addObject("auths", Maps.newHashMap());
        } else {
            view.addObject("auths", AuthInfo.getAuthMap(allAuth, Lists.newArrayList(), false));
        }
        view.addObject("editData", null);
        return view;
    }


    @RequestMapping("/edit/user/{id}")
    public ModelAndView skipUserEdit(@PathVariable("id") String id, HttpServletRequest request) {
        UserEntity internalById = userService.findInternalById(id);
        ModelAndView view = new ModelAndView();
        view.setViewName("admin/save-user.html");
        addAdminModelAndView(view, request);
        List<RoleEntity> roleAll = roleService.findAll();
        if (CollectionUtils.isEmpty(roleAll)) {
            view.addObject("roles", Lists.newArrayList());
        } else {
            view.addObject("roles", roleAll.stream().map(RoleInfo::create).collect(Collectors.toList()));
        }
        view.addObject("editData", UserInfo.create(internalById));
        return view;
    }


    @RequestMapping("/edit/auth/{id}")
    public ModelAndView skipAuthEdit(@PathVariable("id") String id, HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        view.setViewName("admin/save-auth.html");
        addAdminModelAndView(view, request);
        AuthEntity internalById = authService.findInternalById(id);
        List<AuthEntity> fathers = authService.findFathers("0");
        fathers = fathers.stream().filter(k -> !k.getId().equals(id)).collect(Collectors.toList());
        view.addObject("editData", AuthInfo.create(internalById));
        view.addObject("fathers", AuthInfo.createAuthList(fathers));
        return view;
    }

    @RequestMapping("/edit/role/{id}")
    public ModelAndView skipRoleEdit(@PathVariable("id") String id, HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        view.setViewName("admin/save-role.html");
        addAdminModelAndView(view, request);
        RoleEntity internalById = roleService.findInternalById(id);
        List<String> authIds = internalById.getAuthIds();
        boolean disabled = false;
        if ("system".equals(internalById.getEname())) {
            disabled = true;
        }
        if (CollectionUtils.isEmpty(authIds)) {
            view.addObject("auths", AuthInfo.getAuthMap(authService.findAll(), Lists.newArrayList(), disabled));
        } else {
            view.addObject("auths", AuthInfo.getAuthMap(authService.findAll(), authIds, disabled));
        }
        view.addObject("editData", RoleInfo.create(internalById));
        return view;
    }


    @RequestMapping("/settings/personal")
    public ModelAndView userSettings(HttpServletRequest request) {
        ModelAndView view = new ModelAndView("admin/settings-personal.html");

        String cookieValue = PassportHelper.getInstance().getCurrCookie(request);
        AuthContext context = (AuthContext) EhcacheUtil.getInstance().get(Constant.loginUserCacheKey, cookieValue);
        if (Objects.isNull(context)) {
            return new ModelAndView(new RedirectView(webCtx));
        }
        addAdminModelAndView(view, request);
        view.addObject("user", userService.findUserByEname(context.getEname(), null, null));
        return view;
    }


    @RequestMapping("/settings/ext/user")
    public ModelAndView extUserSetting(HttpServletRequest request) {
        ModelAndView view = new ModelAndView("admin/settings-ext-user.html");

        String cookieValue = PassportHelper.getInstance().getCurrCookie(request);
        AuthContext context = (AuthContext) EhcacheUtil.getInstance().get(Constant.loginUserCacheKey, cookieValue);
        if (Objects.isNull(context)) {
            return new ModelAndView(new RedirectView(webCtx));
        }
        addAdminModelAndView(view, request);
        if (context.getRegSourceType() == RegSourceType.qqReg) {
            view.addObject("user", userService.findByQQOpenId(context.getQqOpenId()));
        } else {
            view.addObject("user", userService.findUserByEname(context.getEname(), null, null));
        }

        return view;
    }




    @RequestMapping("/list/auth")
    public ModelAndView authSettings(HttpServletRequest request) {
        ModelAndView view = new ModelAndView("admin/list-auth.html");

        String cookieValue = PassportHelper.getInstance().getCurrCookie(request);
        AuthContext context = (AuthContext) EhcacheUtil.getInstance().get(Constant.loginUserCacheKey, cookieValue);
        if (Objects.isNull(context)) {
            return new ModelAndView(new RedirectView(webCtx));
        }
        addAdminModelAndView(view, request);
        return view;
    }


    @RequestMapping("/list/role")
    public ModelAndView roleSettings(HttpServletRequest request) {
        ModelAndView view = new ModelAndView("admin/list-role.html");

        String cookieValue = PassportHelper.getInstance().getCurrCookie(request);
        AuthContext context = (AuthContext) EhcacheUtil.getInstance().get(Constant.loginUserCacheKey, cookieValue);
        if (Objects.isNull(context)) {
            return new ModelAndView(new RedirectView(webCtx));
        }
        addAdminModelAndView(view, request);
        return view;
    }


    @RequestMapping("/list/user")
    public ModelAndView userList(HttpServletRequest request) {
        ModelAndView view = new ModelAndView("admin/list-user.html");
        String cookieValue = PassportHelper.getInstance().getCurrCookie(request);
        AuthContext context = (AuthContext) EhcacheUtil.getInstance().get(Constant.loginUserCacheKey, cookieValue);
        if (Objects.isNull(context)) {
            return new ModelAndView(new RedirectView(webCtx));
        }
        addAdminModelAndView(view, request);
        return view;
    }


    @RequestMapping("/list/user/delete")
    public ModelAndView userDelList(HttpServletRequest request) {
        ModelAndView view = new ModelAndView("admin/list-user-del.html");
        String cookieValue = PassportHelper.getInstance().getCurrCookie(request);
        AuthContext context = (AuthContext) EhcacheUtil.getInstance().get(Constant.loginUserCacheKey, cookieValue);
        if (Objects.isNull(context)) {
            return new ModelAndView(new RedirectView(webCtx));
        }
        addAdminModelAndView(view, request);
        return view;
    }


}
