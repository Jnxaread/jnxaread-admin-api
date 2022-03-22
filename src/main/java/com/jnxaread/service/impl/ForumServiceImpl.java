package com.jnxaread.service.impl;

import com.jnxaread.bean.Board;
import com.jnxaread.bean.Reply;
import com.jnxaread.dao.wrap.BoardMapperWrap;
import com.jnxaread.dao.wrap.ReplyMapperWrap;
import com.jnxaread.exception.GlobalException;
import com.jnxaread.service.ForumService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static com.jnxaread.constant.UnifiedCode.REPLY_NOT_EXIST;

/**
 * @author 未央
 * @create 2020-06-28 17:22
 */
@Service
public class ForumServiceImpl extends BaseForumServiceImpl implements ForumService {
    @Resource
    private BoardMapperWrap boardMapper;
    @Resource
    private ReplyMapperWrap replyMapper;

    @Override
    public int addBoard(Board newBoard) {
        boardMapper.insertSelective(newBoard);
        return newBoard.getId();
    }

    @Override
    public void deleteReply(Integer id) {
        Reply record = replyMapper.selectByPrimaryKey(id);
        if (record == null || record.getDeleted()) {
            throw new GlobalException(REPLY_NOT_EXIST.getCode(), REPLY_NOT_EXIST.getDescribe());
        }
        record.setDeleted(true);
        replyMapper.updateByPrimaryKey(record);
    }
}
