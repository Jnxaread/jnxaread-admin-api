package com.jnxaread.service;

import com.jnxaread.bean.Board;
import com.jnxaread.bean.User;

/**
 * @author 未央
 * @create 2020-06-28 17:22
 */
public interface ForumService extends BaseForumService {

    int addBoard(Board newBoard);

    void deleteReply(Integer id);

    void updateVisibleOfTopic(Integer id, Integer visible);

    void deleteTopic(Integer id, User admin);

    void updateLockOfTopic(Integer id, boolean lock);
}
