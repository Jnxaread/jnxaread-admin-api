package com.jnxaread.service.impl;

import com.jnxaread.bean.Board;
import com.jnxaread.bean.Reply;
import com.jnxaread.bean.Topic;
import com.jnxaread.dao.wrap.BoardMapperWrap;
import com.jnxaread.dao.wrap.ReplyMapperWrap;
import com.jnxaread.dao.wrap.TopicMapperWrap;
import com.jnxaread.exception.GlobalException;
import com.jnxaread.service.ForumService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static com.jnxaread.constant.UnifiedCode.*;

/**
 * @author 未央
 * @create 2020-06-28 17:22
 */
@Service
public class ForumServiceImpl extends BaseForumServiceImpl implements ForumService {
    @Resource
    private BoardMapperWrap boardMapper;
    @Resource
    private TopicMapperWrap topicMapper;
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

    @Override
    public void updateVisibleOfTopic(Integer id, Integer visible) {
        Topic record = topicMapper.selectByPrimaryKey(id);
        if (record == null || record.getDeleted()) {
            throw new GlobalException(TOPIC_NOT_EXIST.getCode(), TOPIC_NOT_EXIST.getDescribe());
        }
        if (!visible.equals(0) && !visible.equals(1)) {
            throw new GlobalException(VISIBLE_VALUE_INVALID.getCode(), VISIBLE_VALUE_INVALID.getDescribe());
        }
        if (record.getVisible().equals(visible)) {
            return;
        }
        record.setVisible(visible);
        topicMapper.updateByPrimaryKey(record);
    }
}
