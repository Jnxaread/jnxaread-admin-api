package com.jnxaread.controller;

import com.jnxaread.bean.Notice;
import com.jnxaread.bean.User;
import com.jnxaread.bean.wrap.NoticeWrap;
import com.jnxaread.entity.UnifiedResult;
import com.jnxaread.service.NoticeService;
import com.jnxaread.util.ContentUtil;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author 未央
 * @Create 2021-02-10 15:23
 */
@RestController
@RequestMapping("/forum")
public class NoticeController {

    @Resource
    private NoticeService noticeService;

    private final Logger logger = LoggerFactory.getLogger("action");

    /**
     * 获取公告列表接口
     *
     * @return 公告列表
     */
    @RequestMapping("/list/notice")
    public UnifiedResult getNoticeList() {
        List<NoticeWrap> noticeWrapList = noticeService.getNoticeWrapList(null);
        long noticeCount = noticeService.getNoticeCount();
        Map<String, Object> map = new HashMap<>();
        map.put("notices", noticeWrapList);
        map.put("count", noticeCount);
        return UnifiedResult.ok(map);
    }

    /**
     * 获取公告详情接口
     *
     * @param id 公告id
     * @return 公告详细内容
     */
    @RequestMapping("/detail/notice")
    public UnifiedResult getNotice(Integer id) {
        if (id == null) {
            return UnifiedResult.build("400", "参数错误", null);
        }
        NoticeWrap noticeWrap = noticeService.getNoticeWrap(id);
        if (noticeWrap == null) {
            return UnifiedResult.build("400", "公告不存在", null);
        }
        return UnifiedResult.ok(noticeWrap);
    }

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
            return UnifiedResult.build("400", "参数错误", null);
        }

        String regLabel = "^[\\u4e00-\\u9fa5]{2,4}$";
        Pattern pattern = Pattern.compile(regLabel);
        Matcher matcher = pattern.matcher(newNotice.getLabel());
        if (!matcher.matches()) {
            return UnifiedResult.build("400", "公告标签为2至4位汉字", null);
        }

        if (newNotice.getTitle().length() < 4 || newNotice.getTitle().length() > 35) {
            return UnifiedResult.build("400", "公告标题的长度为4至35个字符", null);
        }

        //校验公告内容是否为空
        boolean inspection = ContentUtil.inspection(newNotice.getContent());
        if (!inspection) {
            return UnifiedResult.build("400", "公告内容不能为空！", null);
        }

        if (newNotice.getContent().length() > 16384) {
            return UnifiedResult.build("400", "公告内容的长度不得超过16000个字符", null);
        }

        User admin = (User) session.getAttribute("admin");
        newNotice.setUserId(admin.getId());
        newNotice.setLastUserId(admin.getId());
        Date date = new Date();
        newNotice.setCreateTime(date);
        newNotice.setLastTime(date);
        int noticeId = noticeService.addNotice(newNotice);

        //    789-submitNotice-36
        String logMsg = admin.getId() + "-submitNotice-" + noticeId;
        logger.info(logMsg);

        return UnifiedResult.ok(noticeId);
    }

    @PostMapping("/hide/notice")
    public UnifiedResult hideNotice(Integer id, Integer visible) {
        if (id == null || visible == null) {
            return UnifiedResult.build("400", "", null);
        }
        int status = noticeService.hideNotice(id, visible);
        switch (status) {
            case 0:
                return UnifiedResult.ok();
            case 1:
                return UnifiedResult.build("400", "公告不存在", "");
            case 2:
                return UnifiedResult.build("400", "公告已下架", "");
            case 3:
                return UnifiedResult.build("400", "公告已上架", "");
            default:
                return UnifiedResult.build("500", "未知错误", "");
        }
    }

    @PostMapping("/lock/notice")
    public UnifiedResult lockNotice(Integer id, Integer locked) {
        if (id == null || locked == null) {
            return UnifiedResult.build("400", "参数错误", null);
        }
        int status = noticeService.lockNotice(id, locked);
        switch (status) {
            case 0:
                return UnifiedResult.ok();
            case 1:
                return UnifiedResult.build("400", "公告不存在", "");
            case 2:
                return UnifiedResult.build("400", "公告已锁定", "");
            case 3:
                return UnifiedResult.build("400", "公告已解锁", "");
            default:
                return UnifiedResult.build("500", "未知错误", "");
        }
    }

}
