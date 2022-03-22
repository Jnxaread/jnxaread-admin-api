package com.jnxaread.service;

import com.jnxaread.bean.Notice;

/**
 * @Author 未央
 * @Create 2021-02-10 15:25
 */
public interface NoticeService extends BaseNoticeService {

    int addNotice(Notice newNotice);

    void updateNotice(Notice notice);

    long getNoticeCount();

    void hideNotice(int id, int visible);

    void lockNotice(int id, int locked);

    void upToTop(Integer id);
}
