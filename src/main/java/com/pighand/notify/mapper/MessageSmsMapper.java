package com.pighand.notify.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pighand.framework.spring.base.BaseMapper;
import com.pighand.notify.domain.MessageSmsDomain;
import com.pighand.notify.vo.MessageSmsVO;
import org.apache.ibatis.annotations.Mapper;
import com.pighand.framework.spring.page.PageOrList;

/**
 * 短信消息
 *
 * @author wangshuli
 * @createDate 2023-03-09 11:28:59
 */
@Mapper
public interface MessageSmsMapper extends BaseMapper<MessageSmsDomain> {

    /**
     * 分页或列表
     *
     * @param pageInfo
     * @param messageSmsVO
     * @return
     */
    PageOrList<MessageSmsVO> query(Page pageInfo, MessageSmsVO messageSmsVO);
}
