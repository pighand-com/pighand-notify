package com.pighand.notify.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pighand.framework.spring.base.BaseMapper;
import com.pighand.notify.domain.MessageContentDomain;
import com.pighand.notify.vo.MessageContentVO;
import org.apache.ibatis.annotations.Mapper;
import com.pighand.framework.spring.page.PageOrList;

/**
 * 内容消息
 *
 * @author wangshuli
 * @createDate 2023-03-09 11:28:59
 */
@Mapper
public interface MessageContentMapper extends BaseMapper<MessageContentDomain> {

    /**
     * 分页或列表
     *
     * @param pageInfo
     * @param messageContentVO
     * @return
     */
    PageOrList<MessageContentVO> query(Page pageInfo, MessageContentVO messageContentVO);
}
