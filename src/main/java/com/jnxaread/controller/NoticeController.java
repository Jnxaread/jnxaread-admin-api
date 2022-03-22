package com.jnxaread.controller;

import com.jnxaread.bean.Notice;
import com.jnxaread.bean.User;
import com.jnxaread.bean.wrap.NoticeWrap;
import com.jnxaread.common.UnifiedResult;
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

import static com.jnxaread.constant.UnifiedCode.PARAMETER_INVALID;

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
            return UnifiedResult.build("400", "参数错误");
        }
        NoticeWrap noticeWrap = noticeService.getNoticeWrap(id);
        if (noticeWrap == null) {
            return UnifiedResult.build("400", "公告不存在");
        }
        return UnifiedResult.ok(noticeWrap);
    }

    /**
     * 发布或编辑公告
     *
     * @param session 请求Session对象
     * @param notice  要发布或编辑的公告
     * @return 执行结果及公告id
     */
    @PostMapping("/notice/addOrUpdate")
    public UnifiedResult submitNotice(HttpSession session, Notice notice) {
        if (notice == null) {
            return UnifiedResult.build("400", "参数错误");
        }

        String regLabel = "^[\\u4e00-\\u9fa5]{2,4}$";
        Pattern pattern = Pattern.compile(regLabel);
        Matcher matcher = pattern.matcher(notice.getLabel());
        if (!matcher.matches()) {
            return UnifiedResult.build("400", "公告标签为2至4位汉字");
        }

        if (notice.getTitle().length() < 4 || notice.getTitle().length() > 35) {
            return UnifiedResult.build("400", "公告标题的长度为4至35个字符");
        }

        //校验公告内容是否为空
        boolean inspection = ContentUtil.inspection(notice.getContent());
        if (!inspection) {
            return UnifiedResult.build("400", "公告内容不能为空！");
        }

        if (notice.getContent().length() > 16384) {
            return UnifiedResult.build("400", "公告内容的长度不得超过16000个字符");
        }

        String logMsg;

        User admin = (User) session.getAttribute("admin");
        Date date = new Date();
        Integer noticeId = notice.getId();
        if (noticeId == null) {
            notice.setUserId(admin.getId());
            notice.setLastUserId(admin.getId());
            notice.setCreateTime(date);
            notice.setLastTime(date);
            noticeId = noticeService.addNotice(notice);

            //    789-submitNotice-36
            logMsg = admin.getId() + "-submitNotice-" + noticeId;
        } else {
            notice.setLastUserId(admin.getId());
            notice.setLastTime(date);
            noticeService.updateNotice(notice);

            logMsg = admin.getId() + "-updateNotice-" + noticeId;
        }

        logger.info(logMsg);

        return UnifiedResult.ok(noticeId);
    }

    @PostMapping("/hide/notice")
    public UnifiedResult hideNotice(Integer id, Integer visible) {
        if (id == null || visible == null) {
            return UnifiedResult.build("400", "");
        }
        noticeService.hideNotice(id, visible);
        return UnifiedResult.ok();
    }

    @PostMapping("/lock/notice")
    public UnifiedResult lockNotice(Integer id, Integer locked) {
        if (id == null || locked == null) {
            return UnifiedResult.build("400", "参数错误");
        }
        noticeService.lockNotice(id, locked);
        return UnifiedResult.ok();
    }

    /**
     * 置顶公告接口
     *
     * @param id 公告Id
     * @return 操作结果
     */
    @PostMapping("/notice/upToTop")
    public UnifiedResult upToTop(Integer id) {
        if (id == null) {
            String code = PARAMETER_INVALID.getCode();
            String desc = PARAMETER_INVALID.getDescribe();
            return UnifiedResult.build(code, desc);
        }
        noticeService.upToTop(id);
        return UnifiedResult.ok();
    }

}
