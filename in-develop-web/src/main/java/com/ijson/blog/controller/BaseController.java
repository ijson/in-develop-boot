package com.ijson.blog.controller;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.ijson.blog.dao.entity.*;
import com.ijson.blog.exception.BlogBusinessException;
import com.ijson.blog.exception.BlogBusinessExceptionCode;
import com.ijson.blog.exception.BlogCreateException;
import com.ijson.blog.interceptor.LoginInterceptor;
import com.ijson.blog.model.AuthContext;
import com.ijson.blog.model.AuthInfo;
import com.ijson.blog.model.Constant;
import com.ijson.blog.model.arg.AuthArg;
import com.ijson.blog.service.*;
import com.ijson.blog.util.EhcacheUtil;
import com.ijson.blog.util.PassportHelper;
import com.ijson.blog.util.VerifyCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * desc:
 * version: 6.7
 * Created by cuiyongxu on 2019/8/17 11:31 PM
 */
@Slf4j
@Controller
public class BaseController {


    @Autowired
    protected UserService userService;

    @Autowired
    protected WebSiteService webSiteService;

    @Autowired
    protected AuthService authService;

    @Autowired
    protected RoleService roleService;


    @Value("${web.ctx}")
    protected String webCtx;

    @Value("${web.ename}")
    protected String webEname;


    @Value("${cdn.server}")
    protected String cdnServer;

    @Value("${cdn.upload.path}")
    protected String cdnUploadPath;

    /**
     * 后台使用
     *
     * @param view
     */
    protected void addAdminModelAndView(ModelAndView view, HttpServletRequest request) {
        view.addObject("site", getConfig());
        //TODO 0L
        view.addObject("cmtcount", 0L);
    }


    /**
     * 前台使用
     *
     * @param view
     */
    protected void addViewModelAndView(HttpServletRequest request, ModelAndView view) {

        //网站设置 缓存 5分钟
        view.addObject("site", getConfig());

    }


    /**
     * 获取系统配置项
     *
     * @return
     */
    protected ConfigEntity getConfig() {
        return webSiteService.findAllConfig();
    }

    /**
     * 获取context信息
     *
     * @param request
     * @return
     */
    public AuthContext getContext(HttpServletRequest request) {
        String cookieValue = PassportHelper.getInstance().getCurrCookie(request);
        if (!Strings.isNullOrEmpty(cookieValue)) {
            AuthContext context = (AuthContext) EhcacheUtil.getInstance().get(Constant.loginUserCacheKey, cookieValue);
            if (context != null) {
                return context;
            }
        }
        return null;
    }

    /**
     * 生成验证码
     *
     * @param response
     * @param session
     * @param varCodeKey
     * @param varCodeTime
     * @throws IOException
     */
    protected void generateVerification(HttpServletResponse response, HttpSession session, String varCodeKey, String varCodeTime) throws IOException {
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        // 生成随机字串
        String verifyCode = VerifyCodeUtils.generateVerifyCode(4);
        session.removeAttribute(varCodeKey);
        session.removeAttribute(varCodeTime);

        session.setAttribute(varCodeKey, verifyCode.toLowerCase());
        session.setAttribute(varCodeTime, LocalDateTime.now());

        // 生成图片
        int w = 100, h = 30;
        OutputStream out = response.getOutputStream();
        VerifyCodeUtils.outputImage(w, h, out, verifyCode);
    }

    /**
     * 获取动态菜单
     *
     * @param request
     * @return
     */
    protected Map<AuthArg, List<AuthInfo>> getMenu(HttpServletRequest request) {
        AuthContext context = getContext(request);
        List<AuthEntity> menuAuth = context.getAuths().stream().filter(k -> {
            return k.getMenuType() == Constant.MenuType.menu && k.getShowMenu();
        }).collect(Collectors.toList());
        return AuthInfo.getAuthMap(menuAuth, Lists.newArrayList(), false);
    }

    /**
     * @param request
     * @param handleContextException 自行处理context 异常
     * @param handleAuthException    自行处理权限异常
     * @return
     */
    protected AuthContext regularCheck(HttpServletRequest request, boolean handleContextException, boolean handleAuthException) {
        AuthContext context = getContext(request);
        if (Objects.isNull(context)) {
            if (handleContextException) {
                return null;
            } else {
                throw new BlogBusinessException(BlogBusinessExceptionCode.USER_INFORMATION_ACQUISITION_FAILED);
            }
        }
        if (!LoginInterceptor.isParadigm(context.getPermissionPath(), request.getRequestURI())) {
            if (handleAuthException) {
                return null;
            } else {
                throw new BlogCreateException(BlogBusinessExceptionCode.NO_RIGHT_TO_DO_THIS);
            }
        }
        return context;
    }
}
