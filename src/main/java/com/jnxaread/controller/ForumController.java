package com.jnxaread.controller;

import com.jnxaread.bean.Board;
import com.jnxaread.bean.User;
import com.jnxaread.bean.wrap.TopicWrap;
import com.jnxaread.common.UnifiedResult;
import com.jnxaread.service.ForumService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jnxaread.constant.UnifiedCode.PARAMETER_INVALID;

/**
 * 关于论坛模块的管理接口
 *
 * @author 未央
 * @create 2020-06-28 17:23
 */
@RestController
@RequestMapping("/forum")
public class ForumController {
    private final Logger logger = LoggerFactory.getLogger("action");

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
    public UnifiedResult addBoard(HttpSession session, Board newBoard) {
        if (newBoard == null) {
            return UnifiedResult.build("400", "参数不能为空");
        }
        if (newBoard.getName() == null) {
            return UnifiedResult.build("400", "版块名称不能为空");
        }
        if (newBoard.getDescription() == null) {
            return UnifiedResult.build("400", "版块说明不能为空");
        }
        if (newBoard.getRestricted() == null) {
            return UnifiedResult.build("400", "限制性等级不能为空");
        }

        User admin = (User) session.getAttribute("admin");
        newBoard.setUserId(admin.getId());
        newBoard.setManagerId(admin.getId());
        newBoard.setCreateTime(new Date());
        int boardId = forumService.addBoard(newBoard);

        //    789-addBoard-36
        String logMsg = admin.getId() + "-addBoard-" + boardId;
        logger.info(logMsg);

        return UnifiedResult.ok(boardId);
    }

    /**
     * 获取帖子列表接口
     *
     * @param userId 用户id
     * @param page   列表页码
     * @return 帖子列表和帖子总数
     */
    @PostMapping("/topic/list")
    public UnifiedResult getTopicList(Integer userId, Integer page) {
        if (userId == null || page == null) {
            return UnifiedResult.build(PARAMETER_INVALID.getCode(), PARAMETER_INVALID.getDescribe());
        }

        // level为6，表示获取所有帖子
        Map<String, Object> map = new HashMap<>();
        List<TopicWrap> topicWrapList = forumService.getTopicWrapList(userId, 6, page, 45);

        long topicCount = forumService.getTopicCount();
        map.put("topicList", topicWrapList);
        map.put("topicCount", topicCount);
        return UnifiedResult.ok(map);
    }
}
