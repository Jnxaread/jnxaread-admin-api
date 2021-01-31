package com.jnxaread.controller;

import com.jnxaread.service.ProjectService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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

}
