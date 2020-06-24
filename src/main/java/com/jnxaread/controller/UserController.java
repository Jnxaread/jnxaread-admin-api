package com.jnxaread.controller;

import com.jnxaread.bean.Login;
import com.jnxaread.bean.User;
import com.jnxaread.entity.UnifiedResult;
import com.jnxaread.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

/**
 * 用户管理Controller
 *
 * @author 未央
 * @create 2020-06-23 17:25
 */
@RestController
//@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 管理员登录接口
     *
     * @param request
     * @return
     */
    @PostMapping("/login")
    public UnifiedResult login(HttpServletRequest request) {
        HttpSession session = request.getSession();

        //先判断用户是否已经登录
        if (session.getAttribute("admin") != null) {
            return UnifiedResult.build(400, "管理员已登录", null);
        }

        String account = request.getParameter("account");
        String password = request.getParameter("password");

        User user = userService.getUserByAccount(account);
        if (!account.equals("LiSong-ux") || !password.equals(user.getPassword())) {
            return UnifiedResult.build(400, "用户名或密码错误", null);
        }
        session.setAttribute("admin", user);

        //记录用户登录ip、时间
        Login newLogin = new Login();
        newLogin.setIP(request.getRemoteAddr());
        newLogin.setUserId(user.getId());
        newLogin.setCreateTime(new Date());
        //记录用户登录终端
        newLogin.setTerminal(request.getHeader("User-Agent"));
        newLogin.setSystem(1);
        userService.addLogin(newLogin);

        return UnifiedResult.ok(user);
    }

    @PostMapping("/list/user")
    public UnifiedResult getUserList(){
        List<User> userList = userService.getUserList();
        return UnifiedResult.ok(userList);
    }

}
