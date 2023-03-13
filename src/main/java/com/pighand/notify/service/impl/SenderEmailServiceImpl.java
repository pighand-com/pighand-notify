package com.pighand.notify.service.impl;

import com.pighand.notify.domain.SenderEmailDomain;
import com.pighand.notify.mapper.SenderEmailMapper;
import com.pighand.notify.service.SenderEmailService;
import com.pighand.notify.vo.SenderEmailVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.page.PageType;
import org.springframework.stereotype.Service;

/**
 * 发送邮箱配置
 *
 * @author wangshuli
 * @createDate 2023-03-09 11:28:59
 */
@Service
public class SenderEmailServiceImpl extends BaseServiceImpl<SenderEmailMapper, SenderEmailDomain>
        implements SenderEmailService{

   /**
    * 创建
    *
    * @param senderEmailVO
    * @return
    */
   @Override
   public SenderEmailVO create(SenderEmailVO senderEmailVO) {
        super.mapper.insert(senderEmailVO);

        return senderEmailVO;
   }

   /**
    * 详情
    *
    * @param id
    * @return
    */
   @Override
   public SenderEmailDomain find(Long id) {
        SenderEmailDomain senderEmailDomain = super.mapper.selectById(id);
        return senderEmailDomain;
   }

   /**
    * 分页或列表
    *
    * @param senderEmailVO
    */
   @Override
   public PageOrList<SenderEmailVO> query(SenderEmailVO senderEmailVO) {
        PageOrList pageInfo = senderEmailVO.pageParamOrInit(PageType.NEXT_TOKEN);
        return super.mapper.query(pageInfo, senderEmailVO);
   }

   /**
    * 修改
    *
    * @param senderEmailVO
    */
   @Override
   public void update(SenderEmailVO senderEmailVO) {
        super.mapper.updateById(senderEmailVO);
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
