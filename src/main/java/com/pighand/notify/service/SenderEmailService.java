package com.pighand.notify.service;

import com.pighand.framework.spring.base.BaseService;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.notify.domain.SenderEmailDomain;
import com.pighand.notify.vo.SenderEmailVO;

/**
 * 发送邮箱配置
 *
 * @author wangshuli
 * @createDate 2023-03-09 11:28:59
 */
public interface SenderEmailService extends BaseService<SenderEmailDomain> {

    /**
     * 创建
     *
     * @param senderEmailVO
     * @return
     */
    SenderEmailVO create(SenderEmailVO senderEmailVO);

    /**
     * 详情
     *
     * @param id
     * @return
     */
    SenderEmailDomain find(Long id);

    /**
     * 分页或列表
     *
     * @param senderEmailVO
     */
    PageOrList<SenderEmailVO> query(SenderEmailVO senderEmailVO);

    /**
     * 修改
     *
     * @param senderEmailVO
     */
    void update(SenderEmailVO senderEmailVO);

    /**
     * 删除
     *
     * @param id
     */
    void delete(Long id);
}
