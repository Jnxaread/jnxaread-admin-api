package com.jnxaread.controller;

import com.jnxaread.bean.Project;
import com.jnxaread.bean.User;
import com.jnxaread.bean.wrap.ProjectWrap;
import com.jnxaread.entity.UnifiedResult;
import com.jnxaread.service.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

/**
 * 工程信息管理Controller
 *
 * @Author 未央
 * @Create 2021-01-31 12:12
 */
@RestController
@RequestMapping("/project")
public class ProjectController {
    private final Logger logger = LoggerFactory.getLogger("action");

    @Resource
    private ProjectService projectService;


    /**
     * 获取所有版本信息接口
     *
     * @return 版本信息列表
     */
    @PostMapping("/list/version")
    public UnifiedResult getVersionList() {
        List<ProjectWrap> versionList = projectService.getVersionList();
        return UnifiedResult.ok(versionList);
    }

    /**
     * 发布版本接口
     *
     * @param newProject 新版本对象
     * @return 保存结果
     */
    @PostMapping("/new/version")
    public UnifiedResult addProject(HttpSession session, Project newProject) {
        User admin = (User) session.getAttribute("admin");
        newProject.setUserId(admin.getId());
        newProject.setCreateTime(new Date());
        int projectId = projectService.addProject(newProject);

        String logMsg = admin.getId() + "-addProject-" + projectId;
        logger.info(logMsg);

        return UnifiedResult.ok();
    }

}
