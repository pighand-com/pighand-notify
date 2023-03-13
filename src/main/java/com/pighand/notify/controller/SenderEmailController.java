package com.pighand.notify.controller;

import com.pighand.framework.spring.api.annotation.*;
import com.pighand.framework.spring.api.annotation.validation.ValidationGroup;
import com.pighand.framework.spring.base.BaseController;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.response.Result;
import com.pighand.notify.domain.SenderEmailDomain;
import com.pighand.notify.service.SenderEmailService;
import com.pighand.notify.vo.SenderEmailVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;


/**
 * 发送邮箱配置
 *
 * @author wangshuli
 * @createDate 2023-03-09 11:28:59
 */
@RestController(path = "sender/email", docName = "发送邮箱配置")
public class SenderEmailController extends BaseController<SenderEmailService> {

    /**
     * @param senderEmailVO
     * @return
     */
    @Post(docSummary = "创建", fieldGroup = "senderEmailCreate")
    public Result<SenderEmailVO> create(
        @Validated({ValidationGroup.Create.class}) @RequestBody SenderEmailVO senderEmailVO) {
        senderEmailVO = super.service.create(senderEmailVO);

        return new Result(senderEmailVO);
    }

    /**
     * @param id
     * @return
     */
    @Get(path = "/{id}", docSummary = "详情")
    public Result<SenderEmailDomain> find(@PathVariable(name = "id") Long id) {
        SenderEmailDomain senderEmailDomain = super.service.find(id);

        return new Result(senderEmailDomain);
    }

    /**
     * @param senderEmailVO
     */
    @Get(docSummary = "分页或列表", fieldGroup = "senderEmailQuery")
    public Result<PageOrList<SenderEmailVO>> query(SenderEmailVO senderEmailVO) {
        PageOrList<SenderEmailVO> result = super.service.query(senderEmailVO);

        return new Result(result);
    }

    /**
     * @param senderEmailVO
     */
    @Put(path = "{id}", docSummary = "修改", fieldGroup = "senderEmailUpdate")
    public Result update(@PathVariable(name = "id") Long id, @RequestBody SenderEmailVO senderEmailVO) {
        senderEmailVO.setId(id);
        super.service.update(senderEmailVO);

        return new Result();
    }

    /**
     * @param id
     */
    @Delete(path = "{id}", docSummary = "删除")
    public Result delete(@PathVariable(name = "id") Long id) {
        super.service.delete(id);
        return new Result();
    }
}
