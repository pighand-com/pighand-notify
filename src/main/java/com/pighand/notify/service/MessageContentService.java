package com.pighand.notify.service;

import com.pighand.framework.spring.base.BaseService;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.notify.domain.MessageContentDomain;
import com.pighand.notify.vo.MessageContentVO;

/**
 * 内容消息
 *
 * @author wangshuli
 * @createDate 2023-03-09 11:28:59
 */
public interface MessageContentService extends BaseService<MessageContentDomain> {

    /**
     * 创建
     *
     * @param messageContentVO
     * @return
     */
    MessageContentVO create(MessageContentVO messageContentVO);

    /**
     * 详情
     *
     * @param id
     * @return
     */
    MessageContentDomain find(Long id);

    /**
     * 分页或列表
     *
     * @param messageContentVO
     */
    PageOrList<MessageContentVO> query(MessageContentVO messageContentVO);

    /**
     * 修改
     *
     * @param messageContentVO
     */
    void update(MessageContentVO messageContentVO);

    /**
     * 删除
     *
     * @param id
     */
    void delete(Long id);
}
