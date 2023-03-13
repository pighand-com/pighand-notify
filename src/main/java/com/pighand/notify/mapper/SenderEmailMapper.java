package com.pighand.notify.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pighand.framework.spring.base.BaseMapper;
import com.pighand.notify.domain.SenderEmailDomain;
import com.pighand.notify.vo.SenderEmailVO;
import org.apache.ibatis.annotations.Mapper;
import com.pighand.framework.spring.page.PageOrList;

/**
 * 发送邮箱配置
 *
 * @author wangshuli
 * @createDate 2023-03-09 11:28:59
 */
@Mapper
public interface SenderEmailMapper extends BaseMapper<SenderEmailDomain> {

    /**
     * 分页或列表
     *
     * @param pageInfo
     * @param senderEmailVO
     * @return
     */
    PageOrList<SenderEmailVO> query(Page pageInfo, SenderEmailVO senderEmailVO);
}
