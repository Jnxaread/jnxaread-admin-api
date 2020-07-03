package com.jnxaread.service.impl;

import com.jnxaread.bean.Category;
import com.jnxaread.dao.CategoryMapper;
import com.jnxaread.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 未央
 * @create 2020-06-28 16:00
 */
@Service
public class LibraryServiceImpl extends BaseLibraryServiceImpl implements LibraryService {

    @Autowired(required = false)
    private CategoryMapper categoryMapper;

    @Override
    public int addCategory(Category category) {
        categoryMapper.insertSelective(category);
        return category.getId();
    }

}
