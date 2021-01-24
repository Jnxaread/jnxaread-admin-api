package com.jnxaread.service;

import com.jnxaread.bean.Board;
import com.jnxaread.bean.Notice;

/**
 * @author 未央
 * @create 2020-06-28 17:22
 */
public interface ForumService extends BaseForumService {

    int addBoard(Board newBoard);

    int addNotice(Notice newNotice);

}
