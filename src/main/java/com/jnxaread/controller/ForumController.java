package com.jnxaread.controller;

import com.jnxaread.bean.Board;
import com.jnxaread.bean.Notice;
import com.jnxaread.bean.User;
import com.jnxaread.entity.UnifiedResult;
import com.jnxaread.service.ForumService;
import com.jnxaread.util.ContentUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 关于论坛模块的管理接口
 *
 * @author 未央
 * @create 2020-06-28 17:23
 */
@RestController
@RequestMapping("/forum")
public class ForumController {

    @Resource
    private ForumService forumService;

    /**
     * 发布公告接口
     *
     * @param session   请求session对象
     * @param newNotice 新公告表单数据
     * @return 执行结果及公告ID
     */
    @PostMapping("/new/notice")
    public UnifiedResult submitNotice(HttpSession session, Notice newNotice) {
        if (newNotice == null) {
            return UnifiedResult.build(400, "参数错误", null);
        }

        String regLabel = "^[\\u4e00-\\u9fa5]{2,4}$";
        Pattern pattern = Pattern.compile(regLabel);
        Matcher matcher = pattern.matcher(newNotice.getLabel());
        if (!matcher.matches()) {
            return UnifiedResult.build(400, "公告标签为2至4位汉字", null);
        }

        if (newNotice.getTitle().length() < 4 || newNotice.getTitle().length() > 35) {
            return UnifiedResult.build(400, "公告标题的长度为4至35个字符", null);
        }

        //校验公告内容是否为空
        boolean inspection = ContentUtil.inspection(newNotice.getContent());
        if (!inspection) {
            return UnifiedResult.build(400, "公告内容不能为空！", null);
        }

        if (newNotice.getContent().length() > 16384) {
            return UnifiedResult.build(400, "公告内容的长度不得超过16000个字符", null);
        }

        User admin = (User) session.getAttribute("admin");
        newNotice.setUserId(admin.getId());
        newNotice.setLastUserId(admin.getId());
        Date date = new Date();
        newNotice.setCreateTime(date);
        newNotice.setLastTime(date);
        int noticeId = forumService.addNotice(newNotice);
        return UnifiedResult.ok(noticeId);
    }

    /**
     * 获取版块列表接口
     *
     * @return 版块列表
     */
    @PostMapping("/list/board")
    public UnifiedResult getBoardList() {
        List<Board> boardList = forumService.getBoardList();
        return UnifiedResult.ok(boardList);
    }

    /**
     * 添加版块接口
     *
     * @param newBoard 新版块表单数据
     * @return 新版块ID
     */
    @PostMapping("/new/board")
    public UnifiedResult addBoard(Board newBoard) {
        if (newBoard == null) {
            return UnifiedResult.build(400, "参数不能为空", null);
        } else if (newBoard.getName() == null) {
            return UnifiedResult.build(400, "版块名称不能为空", null);
        } else if (newBoard.getDescription() == null) {
            return UnifiedResult.build(400, "版块说明不能为空", null);
        } else if (newBoard.getRestricted() == null) {
            return UnifiedResult.build(400, "限制性等级不能为空", null);
        }
        newBoard.setCreateTime(new Date());
        int boardId = forumService.addBoard(newBoard);
        return UnifiedResult.ok(boardId);
    }

}
