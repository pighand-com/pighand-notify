package com.pighand.notify.service.impl;

import com.pighand.notify.domain.MessageContentDomain;
import com.pighand.notify.mapper.MessageContentMapper;
import com.pighand.notify.service.MessageContentService;
import com.pighand.notify.vo.MessageContentVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.page.PageType;
import org.springframework.stereotype.Service;

/**
 * 内容消息
 *
 * @author wangshuli
 * @createDate 2023-03-09 11:28:59
 */
@Service
public class MessageContentServiceImpl extends BaseServiceImpl<MessageContentMapper, MessageContentDomain>
        implements MessageContentService{

   /**
    * 创建
    *
    * @param messageContentVO
    * @return
    */
   @Override
   public MessageContentVO create(MessageContentVO messageContentVO) {
        super.mapper.insert(messageContentVO);

        return messageContentVO;
   }

   /**
    * 详情
    *
    * @param id
    * @return
    */
   @Override
   public MessageContentDomain find(Long id) {
        MessageContentDomain messageContentDomain = super.mapper.selectById(id);
        return messageContentDomain;
   }

   /**
    * 分页或列表
    *
    * @param messageContentVO
    */
   @Override
   public PageOrList<MessageContentVO> query(MessageContentVO messageContentVO) {
        PageOrList pageInfo = messageContentVO.pageParamOrInit(PageType.NEXT_TOKEN);
        return super.mapper.query(pageInfo, messageContentVO);
   }

   /**
    * 修改
    *
    * @param messageContentVO
    */
   @Override
   public void update(MessageContentVO messageContentVO) {
        super.mapper.updateById(messageContentVO);
   }

   /**
    * 删除
    *
    * @param id
    */
   @Override
   public void delete(Long id) {
        super.mapper.deleteById(id);
   }
}
