package com.jnxaread.controller;

import com.jnxaread.bean.User;
import com.jnxaread.entity.UnifiedResult;
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

    @Resource
    private UserService userService;

    private final Logger logger = LoggerFactory.getLogger("login");

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
            return UnifiedResult.build("400", "管理员已登录", null);
        }

        String account = request.getParameter("account");
        String password = request.getParameter("password");

        User user = userService.getUserByAccount(account);
        String ciphertext = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!account.equals("LiSong-ux") || !user.getPassword().equals(ciphertext.toUpperCase())) {
            return UnifiedResult.build("400", "用户名或密码错误", null);
        }
        session.setAttribute("admin", user);

        // user:245,time:1667889656335,IP:192.169.2.105,terminal:Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.141 Safari/537.36,system:0
        String loginMsg = user.getId() + "-" + request.getRemoteAddr() + "-1-" + request.getHeader("User-Agent");
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
        if (admin == null) return UnifiedResult.build("400", "用户未登录", null);
        session.removeAttribute("admin");
        return UnifiedResult.ok();
    }

    /**
     * 获取用户列表接口
     *
     * @return 用户列表
     */
    @PostMapping("/list/user")
    public UnifiedResult getUserList() {
        List<User> userList = userService.getUserList();
        return UnifiedResult.ok(userList);
    }

}
