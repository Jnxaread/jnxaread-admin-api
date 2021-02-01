package com.jnxaread.controller;

import com.jnxaread.bean.wrap.ProjectWrap;
import com.jnxaread.entity.UnifiedResult;
import com.jnxaread.service.ProjectService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
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

    @Resource
    private ProjectService projectService;

    @PostMapping("/list/version")
    public UnifiedResult getVersionList() {
        List<ProjectWrap> versionList = projectService.getVersionList();
        return UnifiedResult.ok(versionList);
    }

}
