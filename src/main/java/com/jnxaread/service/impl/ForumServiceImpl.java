package com.jnxaread.service.impl;

import com.jnxaread.bean.Board;
import com.jnxaread.dao.BoardMapper;
import com.jnxaread.service.ForumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 未央
 * @create 2020-06-28 17:22
 */
@Service
public class ForumServiceImpl extends BaseForumServiceImpl implements ForumService {

    @Autowired(required = false)
    private BoardMapper boardMapper;

    @Override
    public int addBoard(Board newBoard) {
        boardMapper.insertSelective(newBoard);
        return newBoard.getId();
    }
}
