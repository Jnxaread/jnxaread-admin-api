package com.jnxaread.service.impl;

import com.jnxaread.bean.Notice;
import com.jnxaread.bean.NoticeExample;
import com.jnxaread.dao.wrap.NoticeMapperWrap;
import com.jnxaread.service.NoticeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author 未央
 * @Create 2021-02-10 15:25
 */
@Service
public class NoticeServiceImpl extends BaseNoticeServiceImpl implements NoticeService {

    @Resource
    private NoticeMapperWrap noticeMapper;

    @Override
    public int addNotice(Notice newNotice) {
        noticeMapper.insertSelective(newNotice);
        return newNotice.getId();
    }

    @Override
    public long getNoticeCount() {
        NoticeExample example = new NoticeExample();
        return noticeMapper.countByExample(example);
    }

    @Override
    public int hideNotice(int id, int visible) {
        Notice notice = noticeMapper.selectByPrimaryKey(id);
        if (notice == null || notice.getDeleted()) {
            return 1;
        }
        if (notice.getVisible() == visible) {
            if (visible == 0) {
                return 2;
            } else if (visible == 1) {
                return 3;
            }
        }
        notice.setVisible(visible);
        noticeMapper.updateByPrimaryKeySelective(notice);
        return 0;
    }

    @Override
    public int lockNotice(int id, int locked) {
        Notice notice = noticeMapper.selectByPrimaryKey(id);
        if (notice == null || notice.getDeleted()) {
            return 1;
        }
        int original = 0;
        if (notice.getLocked()) {
            original = 1;
        }
        if (original == locked) {
            if (locked == 0) {
                return 2;
            } else {
                return 3;
            }
        }
        notice.setLocked(!notice.getLocked());
        noticeMapper.updateByPrimaryKeySelective(notice);
        return 0;
    }

}
