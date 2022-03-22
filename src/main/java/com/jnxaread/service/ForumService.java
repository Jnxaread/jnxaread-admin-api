package com.jnxaread.service;

import com.jnxaread.bean.Board;

/**
 * @author 未央
 * @create 2020-06-28 17:22
 */
public interface ForumService extends BaseForumService {

    int addBoard(Board newBoard);

    void deleteReply(Integer id);
}
