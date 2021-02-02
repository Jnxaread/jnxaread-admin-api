package com.jnxaread.service;

import com.jnxaread.bean.Project;
import com.jnxaread.bean.wrap.ProjectWrap;

import java.util.List;

/**
 * @Author 未央
 * @Create 2021-01-31 12:13
 */
public interface ProjectService extends BaseProjectService {

    List<ProjectWrap> getVersionList();

    void addProject(Project newProject);

}
