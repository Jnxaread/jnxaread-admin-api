package com.jnxaread.service;

import com.jnxaread.bean.Notice;

/**
 * @Author 未央
 * @Create 2021-02-10 15:25
 */
public interface NoticeService extends BaseNoticeService {

    int addNotice(Notice newNotice);

    long getNoticeCount();

}
