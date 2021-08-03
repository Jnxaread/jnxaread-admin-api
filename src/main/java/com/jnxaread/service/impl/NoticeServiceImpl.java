package com.jnxaread.service.impl;

import com.jnxaread.bean.Notice;
import com.jnxaread.bean.NoticeExample;
import com.jnxaread.dao.wrap.BoardMapperWrap;
import com.jnxaread.dao.wrap.NoticeMapperWrap;
import com.jnxaread.entity.GlobalException;
import com.jnxaread.service.NoticeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static com.jnxaread.constant.UnifiedCode.*;

/**
 * @Author 未央
 * @Create 2021-02-10 15:25
 */
@Service
public class NoticeServiceImpl extends BaseNoticeServiceImpl implements NoticeService {
    @Resource
    private NoticeMapperWrap noticeMapper;
    @Resource
    private BoardMapperWrap boardMapper;

    @Override
    public int addNotice(Notice newNotice) {
        noticeMapper.insertSelective(newNotice);
        boardMapper.updateNoticeCountByPrimaryKey(newNotice.getBoardId());
        return newNotice.getId();
    }

    @Override
    public long getNoticeCount() {
        NoticeExample example = new NoticeExample();
        return noticeMapper.countByExample(example);
    }

    @Override
    public void hideNotice(int id, int visible) {
        Notice notice = noticeMapper.selectByPrimaryKey(id);
        if (notice == null || notice.getDeleted()) {
            throw new GlobalException(NOTICE_NOT_EXIST.getCode(), NOTICE_NOT_EXIST.getDescribe());
        }
        if (notice.getVisible() == visible) {
            if (visible == 0) {
                throw new GlobalException(NOTICE_ALREADY_OFF_SHELF.getCode(), NOTICE_ALREADY_OFF_SHELF.getDescribe());
            } else if (visible == 1) {
                throw new GlobalException(NOTICE_ALREADY_ON_SHELF.getCode(), NOTICE_ALREADY_ON_SHELF.getDescribe());
            }
        }
        notice.setVisible(visible);
        noticeMapper.updateByPrimaryKeySelective(notice);
    }

    @Override
    public void lockNotice(int id, int locked) {
        Notice notice = noticeMapper.selectByPrimaryKey(id);
        if (notice == null || notice.getDeleted()) {
            throw new GlobalException(NOTICE_NOT_EXIST.getCode(), NOTICE_NOT_EXIST.getDescribe());
        }
        int original = 0;
        if (notice.getLocked()) {
            original = 1;
        }
        if (original == locked) {
            if (locked == 0) {
                throw new GlobalException(NOTICE_ALREADY_LOCKED.getCode(), NOTICE_ALREADY_LOCKED.getDescribe());
            } else {
                throw new GlobalException(NOTICE_ALREADY_UNLOCKED.getCode(), NOTICE_ALREADY_UNLOCKED.getDescribe());
            }
        }
        notice.setLocked(!notice.getLocked());
        noticeMapper.updateByPrimaryKeySelective(notice);
    }

}
