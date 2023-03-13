package com.pighand.notify.controller;

import com.pighand.framework.spring.api.annotation.*;
import com.pighand.framework.spring.api.annotation.validation.ValidationGroup;
import com.pighand.framework.spring.base.BaseController;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.response.Result;
import com.pighand.notify.domain.MessageSmsDomain;
import com.pighand.notify.service.MessageSmsService;
import com.pighand.notify.vo.MessageSmsVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;


/**
 * 短信消息
 *
 * @author wangshuli
 * @createDate 2023-03-09 11:28:59
 */
@RestController(path = "message/sms", docName = "短信消息")
public class MessageSmsController extends BaseController<MessageSmsService> {

    /**
     * @param messageSmsVO
     * @return
     */
    @Post(docSummary = "创建", fieldGroup = "messageSmsCreate")
    public Result<MessageSmsVO> create(
        @Validated({ValidationGroup.Create.class}) @RequestBody MessageSmsVO messageSmsVO) {
        messageSmsVO = super.service.create(messageSmsVO);

        return new Result(messageSmsVO);
    }

    /**
     * @param id
     * @return
     */
    @Get(path = "/{id}", docSummary = "详情")
    public Result<MessageSmsDomain> find(@PathVariable(name = "id") Long id) {
        MessageSmsDomain messageSmsDomain = super.service.find(id);

        return new Result(messageSmsDomain);
    }

    /**
     * @param messageSmsVO
     */
    @Get(docSummary = "分页或列表", fieldGroup = "messageSmsQuery")
    public Result<PageOrList<MessageSmsVO>> query(MessageSmsVO messageSmsVO) {
        PageOrList<MessageSmsVO> result = super.service.query(messageSmsVO);

        return new Result(result);
    }

    /**
     * @param messageSmsVO
     */
    @Put(path = "{id}", docSummary = "修改", fieldGroup = "messageSmsUpdate")
    public Result update(@PathVariable(name = "id") Long id, @RequestBody MessageSmsVO messageSmsVO) {
        messageSmsVO.setId(id);
        super.service.update(messageSmsVO);

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
