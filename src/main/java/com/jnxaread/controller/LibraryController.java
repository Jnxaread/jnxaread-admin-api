package com.jnxaread.controller;

import com.jnxaread.bean.Category;
import com.jnxaread.bean.User;
import com.jnxaread.entity.UnifiedResult;
import com.jnxaread.service.LibraryService;
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
 * @author 未央
 * @create 2020-06-28 15:58
 */
@RestController
@RequestMapping("/library")
public class LibraryController {
    private final Logger logger= LoggerFactory.getLogger("action");

    @Resource
    private LibraryService libraryService;


    /**
     * 获取所有作品类别接口
     * 该接口不需要进行权限校验
     *
     * @return 作品类别列表
     */
    @PostMapping("/list/category")
    public UnifiedResult getCategoryList() {
        List<Category> categoryList = libraryService.getCategoryList();
        return UnifiedResult.ok(categoryList);
    }

    /**
     * 添加作品分类接口
     *
     * @param newCategory 新作品分类
     * @return 新类别ID
     */
    @PostMapping("/new/category")
    public UnifiedResult addCategory(HttpSession session, Category newCategory) {
        if (newCategory == null) {
            return UnifiedResult.build("400", "参数错误", null);
        } else if (newCategory.getName() == null) {
            return UnifiedResult.build("400", "类别名称不能为空", null);
        } else if (newCategory.getDescription() == null) {
            return UnifiedResult.build("400", "类别说明不能为空", null);
        } else if (newCategory.getRestricted() == null) {
            return UnifiedResult.build("400", "限制性等级不能为空", null);
        }
        User admin = (User) session.getAttribute("admin");
        newCategory.setUserId(admin.getId());
        newCategory.setManagerId(admin.getId());
        newCategory.setCreateTime(new Date());
        int categoryId = libraryService.addCategory(newCategory);

        String logMsg=admin.getId()+"-addCategory-"+categoryId;
        logger.info(logMsg);

        return UnifiedResult.ok(categoryId);
    }

}
