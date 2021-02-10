package com.jnxaread.service.impl;

import com.jnxaread.bean.Notice;
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

}
