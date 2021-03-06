package com.jnxaread.service.impl;

import com.jnxaread.bean.Board;
import com.jnxaread.dao.wrap.BoardMapperWrap;
import com.jnxaread.service.ForumService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author 未央
 * @create 2020-06-28 17:22
 */
@Service
public class ForumServiceImpl extends BaseForumServiceImpl implements ForumService {

    @Resource
    private BoardMapperWrap boardMapper;

    @Override
    public int addBoard(Board newBoard) {
        boardMapper.insertSelective(newBoard);
        return newBoard.getId();
    }
}
