package com.jnxaread.service.impl;

import com.jnxaread.bean.Notice;
import com.jnxaread.bean.NoticeExample;
import com.jnxaread.dao.wrap.BoardMapperWrap;
import com.jnxaread.dao.wrap.NoticeMapperWrap;
import com.jnxaread.exception.GlobalException;
import com.jnxaread.service.NoticeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.List;

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
        Integer maxPosition = noticeMapper.selectMaxPosition();
        newNotice.setPosition(maxPosition == null ? 1 : maxPosition + 1);
        noticeMapper.insertSelective(newNotice);
        boardMapper.updateNoticeCountByPrimaryKey(newNotice.getBoardId());
        return newNotice.getId();
    }

    @Override
    public void updateNotice(Notice notice) {
        notice.setUpdateCount(notice.getUpdateCount()+1);
        noticeMapper.updateByPrimaryKey(notice);
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

    @Override
    public void upToTop(Integer id) {
        Notice record = noticeMapper.selectByPrimaryKey(id);
        if (record == null) {
            throw new GlobalException(NOTICE_NOT_EXIST.getCode(), NOTICE_NOT_EXIST.getDescribe());
        }
        List<Notice> topList = noticeMapper.findListByPosition(1, record.getPosition() - 1);
        for (Notice notice : topList) {
            notice.setPosition(notice.getPosition() + 1);
            noticeMapper.updateByPrimaryKeySelective(notice);
        }
    }

}
