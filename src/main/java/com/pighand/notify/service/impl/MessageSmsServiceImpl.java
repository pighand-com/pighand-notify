package com.pighand.notify.service.impl;

import com.pighand.notify.domain.MessageSmsDomain;
import com.pighand.notify.mapper.MessageSmsMapper;
import com.pighand.notify.service.MessageSmsService;
import com.pighand.notify.vo.MessageSmsVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.page.PageType;
import org.springframework.stereotype.Service;

/**
 * 短信消息
 *
 * @author wangshuli
 * @createDate 2023-03-09 11:28:59
 */
@Service
public class MessageSmsServiceImpl extends BaseServiceImpl<MessageSmsMapper, MessageSmsDomain>
        implements MessageSmsService{

   /**
    * 创建
    *
    * @param messageSmsVO
    * @return
    */
   @Override
   public MessageSmsVO create(MessageSmsVO messageSmsVO) {
        super.mapper.insert(messageSmsVO);

        return messageSmsVO;
   }

   /**
    * 详情
    *
    * @param id
    * @return
    */
   @Override
   public MessageSmsDomain find(Long id) {
        MessageSmsDomain messageSmsDomain = super.mapper.selectById(id);
        return messageSmsDomain;
   }

   /**
    * 分页或列表
    *
    * @param messageSmsVO
    */
   @Override
   public PageOrList<MessageSmsVO> query(MessageSmsVO messageSmsVO) {
        PageOrList pageInfo = messageSmsVO.pageParamOrInit(PageType.NEXT_TOKEN);
        return super.mapper.query(pageInfo, messageSmsVO);
   }

   /**
    * 修改
    *
    * @param messageSmsVO
    */
   @Override
   public void update(MessageSmsVO messageSmsVO) {
        super.mapper.updateById(messageSmsVO);
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
