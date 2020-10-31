package com.jnxaread.controller;

import com.jnxaread.bean.Board;
import com.jnxaread.entity.UnifiedResult;
import com.jnxaread.service.ForumService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author 未央
 * @create 2020-06-28 17:23
 */
@RestController
@RequestMapping("/forum")
public class ForumController {

    @Resource
    private ForumService forumService;

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
