package com.jnxaread.controller;

import com.jnxaread.bean.User;
import com.jnxaread.common.UnifiedResult;
import com.jnxaread.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 用户管理Controller
 *
 * @author 未央
 * @create 2020-06-23 17:25
 */
@RestController
@RequestMapping("/user")
public class UserController {
    private final Logger logger = LoggerFactory.getLogger("action");

    @Resource
    private UserService userService;


    /**
     * 管理员登录接口
     *
     * @param request 请求的request对象
     * @return 用户信息
     */
    @PostMapping("/login")
    public UnifiedResult login(HttpServletRequest request) {
        HttpSession session = request.getSession();

        //先判断用户是否已经登录
        if (session.getAttribute("admin") != null) {
            return UnifiedResult.build("400", "管理员已登录");
        }

        String account = request.getParameter("account");
        String password = request.getParameter("password");

        User user = userService.getUserByAccount(account);
        String ciphertext = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!account.equals("LiSong-ux") || !user.getPassword().equals(ciphertext.toUpperCase())) {
            return UnifiedResult.build("400", "用户名或密码错误");
        }
        session.setAttribute("admin", user);

        String loginMsg = user.getId() + "-login-" + request.getRemoteAddr() + "-" + request.getHeader("User-Agent");
        logger.info(loginMsg);

        return UnifiedResult.ok(user);
    }

    /**
     * 管理员退出登录接口
     *
     * @param session 会话对象
     * @return 退出成功
     */
    @PostMapping("/logout")
    public UnifiedResult logout(HttpSession session) {
        User admin = (User) session.getAttribute("admin");
        if (admin == null) return UnifiedResult.build("400", "用户未登录");
        session.removeAttribute("admin");

        String logMsg = admin.getId() + "-logout";
        logger.info(logMsg);

        return UnifiedResult.ok();
    }

    /**
     * 获取用户列表接口
     *
     * @return 用户列表
     */
    @PostMapping("/list")
    public UnifiedResult getUserList() {
        List<User> userList = userService.getUserList();
        return UnifiedResult.ok(userList);
    }

}
