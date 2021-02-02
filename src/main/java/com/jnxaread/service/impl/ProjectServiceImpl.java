package com.jnxaread.service.impl;

import com.jnxaread.bean.Project;
import com.jnxaread.bean.wrap.ProjectWrap;
import com.jnxaread.dao.wrap.ProjectMapperWrap;
import com.jnxaread.service.ProjectService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author 未央
 * @Create 2021-01-31 12:16
 */
@Service
public class ProjectServiceImpl extends BaseProjectServiceImpl implements ProjectService {

    @Resource
    private ProjectMapperWrap projectMapper;

    @Override
    public List<ProjectWrap> getVersionList() {
        return projectMapper.findListWithUsername();
    }

    @Override
    public void addProject(Project newProject) {
        projectMapper.insertSelective(newProject);
    }
}
