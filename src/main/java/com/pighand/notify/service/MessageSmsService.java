package com.pighand.notify.service;

import com.pighand.framework.spring.base.BaseService;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.notify.domain.MessageSmsDomain;
import com.pighand.notify.vo.MessageSmsVO;

/**
 * 短信消息
 *
 * @author wangshuli
 * @createDate 2023-03-09 11:28:59
 */
public interface MessageSmsService extends BaseService<MessageSmsDomain> {

    /**
     * 创建
     *
     * @param messageSmsVO
     * @return
     */
    MessageSmsVO create(MessageSmsVO messageSmsVO);

    /**
     * 详情
     *
     * @param id
     * @return
     */
    MessageSmsDomain find(Long id);

    /**
     * 分页或列表
     *
     * @param messageSmsVO
     */
    PageOrList<MessageSmsVO> query(MessageSmsVO messageSmsVO);

    /**
     * 修改
     *
     * @param messageSmsVO
     */
    void update(MessageSmsVO messageSmsVO);

    /**
     * 删除
     *
     * @param id
     */
    void delete(Long id);
}
